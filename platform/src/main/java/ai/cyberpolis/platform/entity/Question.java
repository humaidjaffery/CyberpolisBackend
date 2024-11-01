package ai.cyberpolis.platform.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class Question {
    private String question;
    private List<String> choices;
    private String answer;
    private String explanation;
}
