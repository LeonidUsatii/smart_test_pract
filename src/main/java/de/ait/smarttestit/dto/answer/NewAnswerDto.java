package de.ait.smarttestit.dto.answer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "NewAnswer", description = "Adding a new answer")
public record NewAnswerDto(@NotBlank
                           @Schema(description = "Answer text", example = "answer1")
                           String answerText,

                           @Schema(description = "Correct answer", example = "true", defaultValue = "false")
                           boolean isCorrect) {
}
