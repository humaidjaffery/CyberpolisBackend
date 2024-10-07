package ai.cyberpolis.platform.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TestCodeResponse {
    public Boolean hasError;
    public Integer exit_code;
    public String stderr;
    public String stdout;
}
