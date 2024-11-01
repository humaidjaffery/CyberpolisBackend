package ai.cyberpolis.platform.controller;

import ai.cyberpolis.platform.entity.User;
import ai.cyberpolis.platform.entity.UserChatMessage;
import ai.cyberpolis.platform.service.AuthService;
import ai.cyberpolis.platform.service.ChatService;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public UserChatMessage newChatMessage(@PathVariable String moduleId, @RequestBody String prompt) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return chatService.newMessage(moduleId, prompt, user);
    }



}
