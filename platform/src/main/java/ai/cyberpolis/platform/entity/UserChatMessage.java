package ai.cyberpolis.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserChatMessage {
    private String prompt;
    private String reply;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date time;
    private List<String> highlights = new ArrayList<>();
    private int tokens;
}
