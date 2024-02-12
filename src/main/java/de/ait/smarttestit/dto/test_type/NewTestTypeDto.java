package de.ait.smarttestit.dto.test_type;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "NewTestTypeDto " , description = "Adding a new test type")

public record NewTestTypeDto (@NotBlank
        @Schema (description = "Name of the new test type", example = "newTestType")
                              String name) {
    }
