package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.entity.Module;
import ai.cyberpolis.platform.entity.UserModuleRelation;
import ai.cyberpolis.platform.model.CourseModuleListResponse;
import ai.cyberpolis.platform.repository.ModuleRepository;
import jdk.jfr.Label;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    @Lazy
    private  UserModuleService userModuleService;

    public Module addModule(Module module, String courseId) throws Exception {
        //TODO: add image to s3
        Course course = courseService.getCourseService(courseId);

        String moduleId = courseId + "-" + UUID.randomUUID().toString().substring(0, 4);
        while(moduleRepository.findById(moduleId).isPresent()){
            moduleId = courseId + "-" + UUID.randomUUID().toString().substring(0, 4);
        }
        module.setId(moduleId);
        module.setCourse(course);
        return moduleRepository.save(module);
    }

    public Module getModule(String moduleId) throws Exception {
        //TODO: add image to s3
        Optional<Module> module =  moduleRepository.findById(moduleId);

        if(module.isPresent()){
            return module.get();
        } else {
            throw new Exception("Module Not Found");
        }
    }

    public List<CourseModuleListResponse> getAllModulesFromCourse(String courseId, String userEmail) throws Exception {
        Course course = courseService.getCourseService(courseId);
        List<Module> modules = moduleRepository.findAllByCourse(course);
        List<CourseModuleListResponse> courseModuleList = new ArrayList<>();
        for(Module module : modules){
            UserModuleRelation userModuleRelation =  userModuleService.getUserModuleRelation(userEmail, module.getId());
            CourseModuleListResponse courseModuleListResponse = new CourseModuleListResponse(
                    module.getModuleName(),
                    module.getId(),
                    userModuleRelation == null ? false : true,
                    userModuleRelation == null ? false : userModuleRelation.getCompleted()
            );
            courseModuleList.add(courseModuleListResponse);
        }
        return courseModuleList;
    }

//    public Module updateCode(String courseName, String moduleName, String updatedCode) throws Exception {
//        Module module = getModule(courseName, moduleName);
//        module.setModuleUserCode(updatedCode);
//        return moduleRepository.save(module);
//    }
}
