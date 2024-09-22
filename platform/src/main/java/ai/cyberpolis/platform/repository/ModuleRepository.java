package ai.cyberpolis.platform.repository;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.entity.Module;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuleRepository extends MongoRepository<Module, String> {
    Optional<Module> findByCourseAndModuleName(Course course, String moduleName);

    List<Module> findAllByCourse(Course course);
}
