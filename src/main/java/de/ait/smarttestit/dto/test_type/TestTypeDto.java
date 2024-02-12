package de.ait.smarttestit.dto.test_type;

import de.ait.smarttestit.models.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "TestType", description = "Representation of a test type")
public record TestTypeDto(@Schema(description = "Test type id", example = "1")
                          @Positive
                          Long id,

                          @Schema(description = "Name of the test type", example = "newTestType")
                          @NotBlank
                          String name,

                          @Schema(description = "Test id", example = "3")
                          @Positive
                          Long testId ){

    public static TestTypeDto from(TestType testType) {
        Long testId = (testType.getTest() != null) ? testType.getTest().getId() : null;
        return new TestTypeDto(
                testType.getId(),
                testType.getName(),
                testId
        );
    }
    public static List<TestTypeDto> from(Collection<TestType> tests){
        return tests.stream()
                .map(TestTypeDto::from).collect(Collectors.toList());
    }
}
