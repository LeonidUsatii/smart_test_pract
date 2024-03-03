package de.ait.smarttestit.dto.test_type;

import de.ait.smarttestit.models.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "TestType", description = "Representation of a test type")
public record TestTypeDto(@Schema(description = "Test type id", example = "1")
                          @NotNull(message = "The id must not be null")
                          @Positive(message = "The id must be positive")
                          Long id,

                          @Schema(description = "Name of the test type", example = "newTestType", required = true)
                          @NotBlank
                          String name,

                          @Schema(description = "Test id", example = "3")
                          @Positive
                          Long examTaskId) {

    /**
     * Converts a TestType entity to a TestTypeDto object.
     *
     * @param testType The TestType entity to convert.
     * @return The converted TestTypeDto object.
     */
    public static TestTypeDto from(TestType testType) {
        Long examTaskId = testType.getExamTask() != null ? testType.getExamTask().getId() : null;
        return new TestTypeDto(
                testType.getId(),
                testType.getName(),
                examTaskId
        );
    }

    /**
     * Converts a collection of TestType entities to a list of TestTypeDto objects.
     *
     * @param tests The collection of TestType entities to convert.
     * @return The list of converted TestTypeDto objects.
     */
    public static List<TestTypeDto> from(Collection<TestType> tests) {
        return tests.stream()
                .map(TestTypeDto::from)
                .collect(Collectors.toList());
    }
}
