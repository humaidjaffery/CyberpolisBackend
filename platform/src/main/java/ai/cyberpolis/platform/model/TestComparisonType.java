package ai.cyberpolis.platform.model;

import lombok.Data;
import lombok.Getter;

@Getter
public enum TestComparisonType {
    EQUALITY("equality"),
    CLOSE_TO("close_to");

    private final String value;

    TestComparisonType(String value) {
        this.value = value;
    }
}
