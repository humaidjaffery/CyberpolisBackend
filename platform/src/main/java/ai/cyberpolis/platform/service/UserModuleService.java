package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Module;
import ai.cyberpolis.platform.entity.UserModuleRelation;
import ai.cyberpolis.platform.repository.UserModuleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return userModule.isPresent() ? userModule.get() : null;
    }

    public UserModuleRelation addUserModuleRelation(String userEmail, String moduleId) throws Exception {
        Module module = moduleService.getModule(moduleId);

        Boolean[] testsPassed = new Boolean[module.getModuleTests().length];
        Arrays.fill(testsPassed, false);

        UserModuleRelation userModuleRelation = new UserModuleRelation(
                userEmail,
                module.getId(),
                module.getInitialModuleCode(),
                testsPassed,
                false
        );

        UserModuleRelation userModule = userModuleRepository.save(userModuleRelation);
        return userModule;
    }

}
