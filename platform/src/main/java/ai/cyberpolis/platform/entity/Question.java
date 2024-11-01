package ai.cyberpolis.platform.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class Question {
    public String question;
    public List<String> choices;
    public String answer;
    public String explanation;
}
