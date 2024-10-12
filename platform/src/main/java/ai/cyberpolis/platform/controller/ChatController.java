package ai.cyberpolis.platform.controller;

import ai.cyberpolis.platform.model.ChatMessageRequest;
import ai.cyberpolis.platform.service.AuthService;
import ai.cyberpolis.platform.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class ChatController {

    private final AuthService authService;
    private final ChatService chatService;

    public ChatController(AuthService authService, ChatService chatService) {
        this.authService = authService;
        this.chatService = chatService;
    }

    @PostMapping("/newMessage/{moduleId}")
    public String newChatMessage(@PathVariable String moduleId, @RequestBody ChatMessageRequest chatMessageRequest) throws Exception {
        return chatService.newMessage(moduleId, chatMessageRequest);
    }



}
