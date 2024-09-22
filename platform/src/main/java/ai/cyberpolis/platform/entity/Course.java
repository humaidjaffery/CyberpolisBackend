package ai.cyberpolis.platform.entity;

import lombok.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;


@ToString
@Data
@AllArgsConstructor
@Document(collection = "course")
public class Course {

    @Id
    public String courseId;

    public String courseName;
}
