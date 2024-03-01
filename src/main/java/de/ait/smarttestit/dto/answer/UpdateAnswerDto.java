package de.ait.smarttestit.dto.answer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "UpdateAnswer", description = "Updating the answerText, isCorrect of a answer ")
public record UpdateAnswerDto(@NotBlank
                              @Schema(description = "Answer text", example = "answer1")
                              String answerText,
                              @Schema(description = "Correct answer", example = "true", defaultValue = "false")
                              boolean isCorrect) {
}
