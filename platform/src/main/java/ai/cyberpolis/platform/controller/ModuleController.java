package ai.cyberpolis.platform.controller;

import ai.cyberpolis.platform.entity.Module;
import ai.cyberpolis.platform.entity.User;
import ai.cyberpolis.platform.model.CourseModuleListResponse;
import ai.cyberpolis.platform.model.ModuleResponse;
import ai.cyberpolis.platform.service.ModuleService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/module")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }


    @PostMapping("/add/{courseId}")
    public Module addModule(@RequestBody Module module, @PathVariable String courseId) throws Exception {
        return moduleService.addModule(module, courseId);
    }


    @GetMapping("/get/{moduleId}")
    public ModuleResponse getModule(@PathVariable String moduleId) throws Exception {
        return moduleService.getModuleResponse(moduleId);
    }

    @GetMapping("/getAllFromCourse/{courseId}")
    public List<CourseModuleListResponse> getAllModulesFromCourse(@PathVariable String courseId) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return moduleService.getAllModulesFromCourse(courseId, user.getEmail());
    }

}
