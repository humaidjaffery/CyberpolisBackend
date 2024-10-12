package ai.cyberpolis.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class OpenAIRequest {
    public String model;
    public List<Map<String, String>> messages;
}
