package ai.cyberpolis.platform.model;

import lombok.Data;

@Data
public class ChatMessageRequest {
    public String message;
    public String[] highlights; 
}
