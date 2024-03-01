package de.ait.smarttestit.dto.answer;

import de.ait.smarttestit.models.Answer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "Answer", description = "Representation of a answer")
public record AnswerDto(@Positive
                        @Schema(description = "Answer_ID", example = "1")
                        Long id,

                        @NotBlank
                        @Schema(description = "Answer", example = "answer1")
                        String answerText,

                        @Schema(description = "Is answer correct?", example = "true", defaultValue = "false")
                        boolean isCorrect,

                        @Positive
                        @Schema(description = "Question_Id", example = "2")
                        Long questionId) {

    public static AnswerDto from(Answer answer) {
        return new AnswerDto(
                answer.getId(),
                answer.getAnswerText(),
                answer.isCorrect(),
                answer.getQuestion().getId());
    }

    public static List<AnswerDto> from(Collection<Answer> answers) {
        return answers.stream()
                .map(AnswerDto::from)
                .collect(Collectors.toList());
    }
}
