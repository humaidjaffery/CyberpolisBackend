package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Module;
import ai.cyberpolis.platform.entity.User;
import ai.cyberpolis.platform.entity.UserModuleRelation;
import ai.cyberpolis.platform.model.TestCodeResponse;
import ai.cyberpolis.platform.model.UserModuleResponse;
import ai.cyberpolis.platform.repository.UserModuleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.*;

@Service
public class UserModuleService {
    @Autowired
    private UserModuleRepository userModuleRepository;

    private final ModuleService moduleService;

    private final AuthService authService;

    public UserModuleService(ModuleService moduleService, AuthService authService) {
        this.moduleService = moduleService;
        this.authService = authService;
    }


    public List<UserModuleRelation> getAllModulesFromUserEmail(String userEmail){
        return userModuleRepository.findAllByUserEmail(userEmail);
    }

    public Integer checkUserModuleRelation(String userEmail, String moduleId) throws Exception {
        Module module = moduleService.getModule(moduleId);
        Optional<UserModuleRelation> userModule = userModuleRepository.findByUserEmailAndModuleId(userEmail, module.getId());

        if(userModule.isEmpty()){
            return 0;
        } else if (userModule.get().getCompleted()) {
            return 2;
        } else {
            return 1;
        }
    }

    public UserModuleResponse getUserModuleRelation(String userEmail, String moduleId) throws Exception {
        Module module = moduleService.getModule(moduleId);
        Optional<UserModuleRelation> userModule = userModuleRepository.findByUserEmailAndModuleId(userEmail, module.getId());

        if(userModule.isEmpty()){
            addUserModuleRelation(userEmail, moduleId);
            return getUserModuleRelation(userEmail, moduleId);
        }

        return UserModuleResponse.builder()
                .moduleUserCode(userModule.get().getModuleUserCode())
                .completed(userModule.get().getCompleted())
                .messageHistory(userModule.get().getMessageHistory())
                .currency(authService.getTokens(userEmail))
                .testsPassed(userModule.get().getTestsPassed())
                .questionsCorrect(userModule.get().getQuestionsCorrect())
                .build();

    }

    public UserModuleRelation addUserModuleRelation(String userEmail, String moduleId) throws Exception {
        Module module = moduleService.getModule(moduleId);

        ArrayList<Boolean> testsPassed = new ArrayList<>();
        if(module.getModuleTests() != null){
            for(int i=0; i<module.getModuleTests().size(); i++){
                testsPassed.add(false);
            }
        }

        ArrayList<Boolean> questionsCorrect = new ArrayList<>();
        if(module.getModuleTests() != null){
            for(int i=0; i<module.getModuleTests().size(); i++){
                questionsCorrect.add(false);
            }
        }

        String id = userEmail + moduleId;

        UserModuleRelation userModuleRelation = new UserModuleRelation(
                id,
                userEmail,
                module.getId(),
                module.getInitialModuleCode(),
                testsPassed,
                false,
                new ArrayList<>(),
                questionsCorrect
        );

        return userModuleRepository.save(userModuleRelation);
    }


    public List<TestCodeResponse> testCode(String userEmail, String moduleId, List<String> code) throws Exception {
        Module module = moduleService.getModule(moduleId);

        code.addAll(module.getHiddenModuleCode());
        System.out.println(module);
        System.out.println(code);

        //Processing code and tests
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("code", code);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArguments = objectMapper.writeValueAsString(arguments);

        //Create Isolated Docker environment for security, copy "userCode.py" and "codeTest.py" into
        //this docker container and only run codeTest.py with arguments
        ProcessBuilder pb = new ProcessBuilder(
                "docker", "run", "--rm", "-i",
                "--network", "none",
                "--cpu-quota", "50000",
//                "--memory", "512m",
                "codetestcontainer");

        Process process = pb.start();

        try (OutputStream stdin = process.getOutputStream()) {
            stdin.write(jsonArguments.getBytes());
            stdin.flush();
        } catch (Exception e) {
            System.out.println("ERROR PASSING INPUT " + e.getMessage());
        }

        pb.redirectErrorStream(true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();
        System.out.println(exitCode);
        System.out.println(output);

        JsonNode rootNode = objectMapper.readTree(output.toString()).get("results");

        UserModuleRelation userModuleRelation = userModuleRepository.findByUserEmailAndModuleId(userEmail, moduleId).orElseThrow(Exception::new);
        ArrayList<Boolean> testPassed = userModuleRelation.getTestsPassed();
        List<TestCodeResponse> testCodeResponses = new ArrayList<>();
        for(int i=0; i<rootNode.size(); i++) {
            System.out.println(rootNode.get(i));
            TestCodeResponse testCodeResponse = TestCodeResponse.builder()
                    .input(rootNode.path(i).get("input").asText())
                    .expected_output(rootNode.path(i).get("expected_output").asText())
                    .stdout(rootNode.path(i).get("stdout").asText())
                    .stderr(rootNode.path(i).get("stderr").asText())
                    .passed(rootNode.path(i).get("passed").asBoolean())
//                    .exit_code(rootNode.path(i).get("exit_code").asInt())
                    .build();
            testCodeResponses.add(testCodeResponse);

//            if(!testPassed.get(i) && testCodeResponse.getPassed()){
//                testPassed.set(i, true);
//            }
        }

        userModuleRelation.setTestsPassed(testPassed);
        updateUserModuleRelation(userModuleRelation);

        return testCodeResponses;
    }

    public ResponseEntity saveCode(String userEmail, String moduleId, List<String> code) throws Exception {
        UserModuleRelation userModuleRelation = userModuleRepository.findByUserEmailAndModuleId(userEmail, moduleId).orElseThrow(Exception::new);
        userModuleRelation.setModuleUserCode(code);
        userModuleRepository.save(userModuleRelation);
        return ResponseEntity.ok().build();
    }


    public ArrayList<Boolean> updateCorrectQuestion(ArrayList<Boolean> correctQuestions, User user, String moduleId) throws Exception {
        UserModuleRelation userModuleRelation = userModuleRepository.findByUserEmailAndModuleId(user.getEmail(), moduleId).orElseThrow(Exception::new);
        if(correctQuestions.size() != userModuleRelation.getQuestionsCorrect().size()){
            throw new Exception("Format Error. Number Of Questions do not align");
        }
        if(correctQuestions == userModuleRelation.getQuestionsCorrect()){
            throw new Exception("No Question change recorded");
        }
        authService.addTokens(user, 1500);
        userModuleRelation.setQuestionsCorrect(correctQuestions);
        return userModuleRepository.save(userModuleRelation).getQuestionsCorrect();
    }

    public void updateUserModuleRelation(UserModuleRelation userModuleRelation) throws Exception {
        userModuleRepository.save(userModuleRelation);
    }


}
