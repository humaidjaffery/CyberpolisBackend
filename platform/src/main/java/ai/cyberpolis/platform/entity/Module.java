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
    @Id
    private String id;

    private String previousModule;

    private String nextModule;

    private String moduleName;

    private String moduleContent;

    private List<String> hiddenModuleCode;

    private List<String> initialModuleCode;

    private List<String> moduleCodeSolution = new ArrayList<>();

    private List<String> blocks = new ArrayList<>();

    private List<List<Object>> moduleTests;

    private String backgroundImageUrl;

    private List<Question> questions = new ArrayList<>();

    private List<List<String>> mixAndMatch;

    private String interactiveType;
}
