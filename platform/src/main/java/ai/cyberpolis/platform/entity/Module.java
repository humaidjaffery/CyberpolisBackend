package ai.cyberpolis.platform.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Getter
@Setter
@ToString
@Data
@Document(collection = "module")
public class Module {

    @Id
    public int moduleId;

    public String moduleContentMarkdown;

    public String moduleUserCode;

    public String moduleCodeSolution;

    public String moduleTests;

    public boolean hasPassed;

    public byte[] backgroundImage;


}
