package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Module;
import ai.cyberpolis.platform.entity.UserModuleRelation;
import ai.cyberpolis.platform.model.TestCodeResponse;
import ai.cyberpolis.platform.repository.UserModuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

@Service
public class UserModuleService {
    @Autowired
    private UserModuleRepository userModuleRepository;

    @Autowired
    private ModuleService moduleService;


    public List<UserModuleRelation> getAllModulesFromUserEmail(String userEmail){
        return userModuleRepository.findAllByUserEmail(userEmail);
    }

    public UserModuleRelation getUserModuleRelation(String userEmail, String moduleId) throws Exception {
        Module module = moduleService.getModule(moduleId);
        Optional<UserModuleRelation> userModule = userModuleRepository.findByUserEmailAndModuleId(userEmail, module.getId());
        return userModule.orElse(null);
    }

    public UserModuleRelation addUserModuleRelation(String userEmail, String moduleId) throws Exception {
        Module module = moduleService.getModule(moduleId);

        Boolean[] testsPassed = new Boolean[module.getModuleTests().size()];
        Arrays.fill(testsPassed, false);

        Boolean[] questionsCorrect = new Boolean[module.getQuestions().size()];
        Arrays.fill(questionsCorrect, false);

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


    public String testCode(String userEmail, String moduleId, List<String> code) throws Exception {
        Module module = moduleService.getModule(moduleId);
        List<List<Object>> tests = module.getModuleTests();

        //Processing code and tests
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("code", code);
        arguments.put("tests", tests);
        arguments.put("packages", module.getImports());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArguments = objectMapper.writeValueAsString(arguments);

        //Create Isolated Docker environment for security, copy "userCode.py" and "codeTest.py" into
        //this docker container and only run codeTest.py with arguments
        ProcessBuilder pb = new ProcessBuilder("docker", "run", "--rm", "-i", "codetestcontainer");

        Process process = pb.start();

        OutputStream stdin = process.getOutputStream();
        stdin.write(jsonArguments.getBytes());
        stdin.flush();
        stdin.close();

        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String stdoutLine;
        StringBuilder stdout = new StringBuilder();
        while ((stdoutLine = stdoutReader.readLine()) != null) {
            stdout.append(stdoutLine).append("\n");
        }

        String stderrLine;
        StringBuilder stderr = new StringBuilder();
        while ((stderrLine = stderrReader.readLine()) != null) {
            stderr.append(stderrLine).append("\n");
        }

        int exitCode = process.waitFor();
        System.out.println(exitCode);
        System.out.println(stderr);
        System.out.println(stdout);

        TestCodeResponse testCodeResponse = new TestCodeResponse(
                false,
                exitCode,
                stdout.toString(),
                stderr.toString()
        );
        return stdout.toString();
    }


    public Boolean[] updateCorrectQuestion(Boolean[] correctQuestions, String userEmail, String moduleId) throws Exception {
        UserModuleRelation userModuleRelation = getUserModuleRelation(userEmail, moduleId);

        if(correctQuestions.length != userModuleRelation.getQuestionsCorrect().length){
            throw new Exception("Format Error. Number Of questions do not align");
        }

        userModuleRelation.setQuestionsCorrect(correctQuestions);
        return userModuleRepository.save(userModuleRelation).getQuestionsCorrect();
    }

}
