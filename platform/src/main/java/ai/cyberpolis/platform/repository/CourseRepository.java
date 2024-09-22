package ai.cyberpolis.platform.repository;

import ai.cyberpolis.platform.entity.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
}
