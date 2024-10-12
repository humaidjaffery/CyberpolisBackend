package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Module;
import ai.cyberpolis.platform.model.ChatMessageRequest;
import ai.cyberpolis.platform.model.OpenAIRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class ChatService {

    private final ModuleService moduleService;

    @Value("${openai.api-key}")
    private String openai_api_key;

    @Value("${openai.model}")
    private String openai_api_model;

    public ChatService(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    public String newMessage(String moduleId, ChatMessageRequest chatMessageRequest) throws Exception {
        Module module = moduleService.getModule(moduleId);

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", chatMessageRequest.getMessage());

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(message);

        OpenAIRequest openAIRequest = new OpenAIRequest(openai_api_model, messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(openAIRequest);

        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completio"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openai_api_key)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
