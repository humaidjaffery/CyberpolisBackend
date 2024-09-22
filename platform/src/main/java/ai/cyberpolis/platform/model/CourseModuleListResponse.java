package ai.cyberpolis.platform.model;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;

import java.util.UUID;

@AllArgsConstructor
public class CourseModuleListResponse {
    public String moduleName;
    public String moduleId;
    public Boolean hasStarted;
    public Boolean hasCompleted;
}
