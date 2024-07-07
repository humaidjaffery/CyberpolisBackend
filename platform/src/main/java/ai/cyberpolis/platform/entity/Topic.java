package ai.cyberpolis.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Topic {
    @Id
    @SequenceGenerator(name = "topicId", sequenceName = "topicId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topicId")
    public int topicId;

    public int topicName;

}
