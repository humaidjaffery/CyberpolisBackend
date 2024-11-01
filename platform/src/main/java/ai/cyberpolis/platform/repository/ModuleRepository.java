package ai.cyberpolis.platform.repository;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.entity.Module;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuleRepository extends MongoRepository<Module, String> {
    @Query("{ 'id' : { $regex: '^?0' } }")
    List<Module> findAllByCourseIdPrefix(String courseIdPrefix);
}
