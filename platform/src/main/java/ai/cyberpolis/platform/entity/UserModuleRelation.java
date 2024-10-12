package ai.cyberpolis.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_module")
public class UserModuleRelation {
    @Id
    private String id;
    private String userEmail;
    private String moduleId;
    private List<String> moduleUserCode;
    private Boolean[] testsPassed;
    private Boolean completed;
    private List<UserChatMessage> messageHistory = new ArrayList<>();
    private Boolean[] questionsCorrect;
}
