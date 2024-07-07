package ai.cyberpolis.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moduleId")
    @SequenceGenerator(name = "moduleId", sequenceName = "moduleId")
    public int moduleId;

    public String moduleContentMarkdown;

    public String moduleUserCode;

    public String moduleCodeSolution;

    public String moduleTests;

    public boolean hasPassed;

    @Lob
    public byte[] backgroundImage;


}
