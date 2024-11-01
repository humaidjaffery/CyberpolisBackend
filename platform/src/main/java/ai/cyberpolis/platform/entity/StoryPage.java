package ai.cyberpolis.platform.entity;


import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@ToString
@Data
@Document(collection = "story_page")
public class StoryPage {

    @Id
    private long storyPageId;

    private String text;

    private byte[] image;

    private long nextStoryPageId;


}

