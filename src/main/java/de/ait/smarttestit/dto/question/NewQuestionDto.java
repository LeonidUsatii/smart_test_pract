package de.ait.smarttestit.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(name = "NewQuestion", description = "Adding a new question")
public record NewQuestionDto(@NotBlank
                             @Schema(description = "Question text", example = "What is an interface in Java?")
                             String questionText,

                             @Positive
                             @Schema(description = "Level of question", example = "1")
                             int level) {
}