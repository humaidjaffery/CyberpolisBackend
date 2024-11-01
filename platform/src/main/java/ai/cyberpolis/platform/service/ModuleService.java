package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.entity.Module;
import ai.cyberpolis.platform.entity.UserModuleRelation;
import ai.cyberpolis.platform.model.CourseModuleListResponse;
import ai.cyberpolis.platform.model.ModuleResponse;
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
    private final ModuleRepository moduleRepository;

    @Autowired
    private final CourseService courseService;

    @Lazy
    private final UserModuleService userModuleService;

    public ModuleService(ModuleRepository moduleRepository, CourseService courseService, UserModuleService userModuleService) {
        this.moduleRepository = moduleRepository;
        this.courseService = courseService;
        this.userModuleService = userModuleService;
    }

    public Module addModule(Module module, String courseId) throws Exception {
        //TODO: add image to s3
        Course course = courseService.getCourseService(courseId);

        String moduleId = courseId + "-" + UUID.randomUUID().toString().substring(0, 4);
        while(moduleRepository.findById(moduleId).isPresent()){
            moduleId = courseId + "-" + UUID.randomUUID().toString().substring(0, 4);
        }
        module.setId(moduleId);
        return moduleRepository.save(module);
    }

    public Module getModule(String moduleId) throws Exception {
        //TODO: add image to s3
        return moduleRepository.findById(moduleId).orElseThrow(Exception::new);
    }

    public ModuleResponse getModuleResponse(String moduleId) throws Exception {
        //TODO: add image to s3
        Module module = moduleRepository.findById(moduleId).orElseThrow(Exception::new);

        Course course = courseService.getCourseService(module.getId().split("-")[0]);

        return ModuleResponse.builder()
                .course(course)
                .moduleName(module.getModuleName())
                .moduleContent(module.getModuleContent())
                .initialModuleCode(module.getInitialModuleCode())
                .moduleCodeSolution(module.getModuleCodeSolution())
                .blocks(module.getBlocks())
                .moduleTests(module.getModuleTests())
                .backgroundImageUrl(module.getBackgroundImageUrl())
                .questions(module.getQuestions())
                .mixAndMatch(module.getMixAndMatch())
                .interactive(module.getInteractiveType())
                .build();
    }

    public List<CourseModuleListResponse> getAllModulesFromCourse(String courseId, String userEmail) throws Exception {
        List<Module> modules = moduleRepository.findAllByCourseIdPrefix(courseId);
        List<CourseModuleListResponse> courseModuleList = new ArrayList<>();
        for(Module module : modules){
            Integer userModuleRelStatus =  userModuleService.checkUserModuleRelation(userEmail, module.getId());
            CourseModuleListResponse courseModuleListResponse = new CourseModuleListResponse(
                    module.getModuleName(),
                    module.getId(),
                    userModuleRelStatus > 0,
                    userModuleRelStatus > 1
            );
            courseModuleList.add(courseModuleListResponse);
        }
        return courseModuleList;
    }
}
