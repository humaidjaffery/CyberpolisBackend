package ai.cyberpolis.platform.controller;

import ai.cyberpolis.platform.entity.User;
import ai.cyberpolis.platform.entity.UserModuleRelation;
import ai.cyberpolis.platform.model.TestCodeResponse;
import ai.cyberpolis.platform.model.UserModuleResponse;
import ai.cyberpolis.platform.service.UserModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/userModule")
public class UserModuleController {

    @Autowired
    private UserModuleService userModuleService;

    @GetMapping("/getAll/{userEmail}")
    public List<UserModuleRelation> getAllUserModuleInformation(@PathVariable String userEmail){
        return userModuleService.getAllModulesFromUserEmail(userEmail);
    }

    @GetMapping("/get/{moduleId}")
    public UserModuleResponse getUserModuleRelation(@PathVariable String moduleId) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userModuleService.getUserModuleRelation(user.getEmail(), moduleId);
    }

    @PostMapping("/add/{moduleId}")
    public UserModuleRelation addUserModuleRelation(@PathVariable String moduleId) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userModuleService.addUserModuleRelation(user.getEmail(), moduleId);
    }

    @PostMapping("/runCode/{moduleId}")
    public List<TestCodeResponse> runCode(@PathVariable String moduleId, @RequestBody List<String> code) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userModuleService.testCode(user.getEmail(), moduleId, code);
    }

    @PutMapping("/saveCode/{moduleId}")
    public ResponseEntity saveCode(@PathVariable String moduleId, @RequestBody List<String> code) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userModuleService.saveCode(user.getEmail(), moduleId, code);
    }

    @PutMapping("/update/correctQuestion/{moduleId}")
    public ArrayList<Boolean> updatecorrectQuestion(@RequestBody ArrayList<Boolean> questions, @PathVariable String moduleId) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userModuleService.updateCorrectQuestion(questions, user, moduleId);
    }
}
