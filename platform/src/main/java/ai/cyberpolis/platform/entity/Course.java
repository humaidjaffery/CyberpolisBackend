package ai.cyberpolis.platform.entity;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@ToString
@Data
@AllArgsConstructor
@Document(collection = "course")
public class Course {
    @Id
    public String courseName;

}
