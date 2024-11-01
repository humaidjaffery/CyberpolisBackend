package ai.cyberpolis.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestCodeResponse {
    private Integer exit_code;
    private String stderr;
    private String stdout;
    private String input;
    private String expected_output;
    private Boolean passed;
}
