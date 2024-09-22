package ai.cyberpolis.platform.entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@ToString
@Data
@Document(collection = "module")
public class Module {
    public Course course;

    @Id
    public String id;

    public String moduleName;

    public String moduleContentMarkdown;

    public String initialModuleCode;

    public String moduleCodeSolution;

    public String[] moduleTests;

//    public byte[] backgroundImage;
}
