package ai.cyberpolis.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Table
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class StoryPage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storyId")
    @SequenceGenerator(name = "storyId", sequenceName = "storyId")
    public long storyPageId;

    public String text;

    @Lob
    public byte[] image;

    public long nextStoryPageId;


}