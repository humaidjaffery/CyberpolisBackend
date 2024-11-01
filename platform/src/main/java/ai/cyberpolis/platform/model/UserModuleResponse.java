package ai.cyberpolis.platform.model;

import ai.cyberpolis.platform.entity.UserChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModuleResponse {
    private List<String> moduleUserCode;
    private ArrayList<Boolean> testsPassed;
    private Boolean completed;
    private List<UserChatMessage> messageHistory = new ArrayList<>();
    private ArrayList<Boolean> questionsCorrect;
    private int currency;
}
