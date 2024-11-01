package ai.cyberpolis.platform.model;

import ai.cyberpolis.platform.entity.Course;
import ai.cyberpolis.platform.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuleResponse {
    public Course course;

    public String moduleName;

    public String previousModule;

    public String nextModule;

    public String moduleContent;

    public List<String> initialModuleCode;

    public List<String> moduleCodeSolution = new ArrayList<>();

    public List<String> blocks = new ArrayList<>();

    public List<List<Object>> moduleTests;

    public String backgroundImageUrl;

    public List<Question> questions = new ArrayList<>();

    public List<List<String>> mixAndMatch;

    public String interactive;
}
