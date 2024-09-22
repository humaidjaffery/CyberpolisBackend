package ai.cyberpolis.platform.repository;

import ai.cyberpolis.platform.entity.UserModuleRelation;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserModuleRepository extends MongoRepository<UserModuleRelation, ObjectId> {
    Optional<UserModuleRelation> findByUserEmailAndModuleId(String userEmail, String ModuleId);

    List<UserModuleRelation> findAllByUserEmail(String userEmail);
}
