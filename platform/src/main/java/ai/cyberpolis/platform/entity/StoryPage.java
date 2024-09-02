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
    public long storyPageId;

    public String text;

    public byte[] image;

    public long nextStoryPageId;


}

