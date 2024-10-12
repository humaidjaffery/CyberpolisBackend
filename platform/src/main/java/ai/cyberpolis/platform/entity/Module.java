package ai.cyberpolis.platform.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@ToString
@Data
@Document(collection = "module")
public class Module {
    public Course course;

    @Id
    public String id;

    public String moduleName;

    public List<Object> moduleContent;

    public List<String> initialModuleHiddenCode;

    public List<String> initialModuleCode;

    public List<String> imports;

    public String moduleCodeSolution;

    public List<List<Object>> moduleTests;

    public String backgroundImageUrl;

    public List<Question> questions = new ArrayList<>();

    public List<Map<String, String>> mixAndMatch;

    public Object interactive;
}
