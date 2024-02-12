package de.ait.smarttestit.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(name = "UpdateQuestion", description = "Updating the name, level or testTypeId of a question ")
public record UpdateQuestionDto (@NotBlank
                                 @Schema(description = "Update question text",
                                 example = "What is the difference between == and equals?")
                                 String questionText,

                                 @Positive
                                 @Schema(description = "Update level of question",example = "2")
                                 int level,

                                 @Positive
                                 @Schema(description = "Update test type id", example = "5")
                                 Long testTypeId) {
}