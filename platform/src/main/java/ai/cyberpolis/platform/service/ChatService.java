package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.Module;
import ai.cyberpolis.platform.entity.User;
import ai.cyberpolis.platform.entity.UserChatMessage;
import ai.cyberpolis.platform.entity.UserModuleRelation;
import ai.cyberpolis.platform.model.OpenAIRequest;
import ai.cyberpolis.platform.model.UserModuleResponse;
import ai.cyberpolis.platform.repository.UserModuleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.json.JsonObject;
import org.bson.json.JsonParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class ChatService {
    private final AuthService authService;

    private final UserModuleService userModuleService;
    private final UserModuleRepository userModuleRepository;

    @Value("${openai.api-key}")
    private String openai_api_key;

    @Value("${openai.model}")
    private String openai_api_model;

    public ChatService(AuthService authService, UserModuleService userModuleService, UserModuleRepository userModuleRepository) {
        this.authService = authService;
        this.userModuleService = userModuleService;
        this.userModuleRepository = userModuleRepository;
    }

    public UserChatMessage newMessage(String moduleId, String prompt, User user) throws Exception {
        UserModuleRelation userModuleRelation = userModuleRepository.findByUserEmailAndModuleId(user.getEmail(), moduleId).orElseThrow(Exception::new);

        List<Map<String, String>> messages = new ArrayList<>();

        for(UserChatMessage m : userModuleRelation.getMessageHistory()){
            messages.add(new HashMap<String, String>(){{put("role", "user"); put("content", m.getPrompt());}});
            messages.add(new HashMap<String, String>(){{put("role", "assistant"); put("content", m.getReply());}});
        }
        messages.add(new HashMap<String, String>(){{put("role", "user"); put("content", prompt);}});

        OpenAIRequest openAIRequest = new OpenAIRequest(openai_api_model, messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(openAIRequest);

        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openai_api_key)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        JsonNode rootNode = objectMapper.readTree(response.body());

        //subtract tokens
        authService.subtractTokens(user, rootNode.path("usage").path("total_tokens").asInt());

        //update user message history
        UserChatMessage userChatMessage = new UserChatMessage(
                prompt,
                rootNode.path("choices").get(0).path("message").path("content").asText(),
                new Date(),
                rootNode.path("usage").path("total_tokens").asInt()
        );
        List<UserChatMessage> history = userModuleRelation.getMessageHistory();
        history.add(userChatMessage);
        userModuleRelation.setMessageHistory(history);
        userModuleService.updateUserModuleRelation(userModuleRelation);
        return userChatMessage;
    }


}
