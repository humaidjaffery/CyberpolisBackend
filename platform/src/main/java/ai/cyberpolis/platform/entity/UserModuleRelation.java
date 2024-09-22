package ai.cyberpolis.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@Document(collection = "user_module")
public class UserModuleRelation {
    public String userEmail;

    public String moduleId;

    public String moduleUserCode;

    public Boolean[] testsPassed;

    public Boolean completed;

}
