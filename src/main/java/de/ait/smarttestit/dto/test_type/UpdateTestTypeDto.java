package de.ait.smarttestit.dto.test_type;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "UpdateTestType", description = "Updating the name of a test type")
public record UpdateTestTypeDto(@Schema(description = "Update test type name", example = "newTestType")
                                @NotBlank
                                String name) {
}
