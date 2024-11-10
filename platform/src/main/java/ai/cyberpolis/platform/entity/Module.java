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
    public String id;

    public String previous;

    public String next;

    public String moduleName;

    public String moduleContent;

    public List<String> hiddenModuleCode;

    public List<String> initialModuleCode;

    public List<String> moduleCodeSolution = new ArrayList<>();

    public List<String> blocks = new ArrayList<>();

    public List<List<Object>> moduleTests;

    public List<Question> questions = new ArrayList<>();

    public List<List<String>> mixAndMatch;

    public String interactiveType;
}
