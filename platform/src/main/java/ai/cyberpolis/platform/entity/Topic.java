package ai.cyberpolis.platform.entity;


import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@ToString
@Data
@Document(collection = "Topic")
public class Topic {
    @Id
    public int topicId;

    public int topicName;
}
