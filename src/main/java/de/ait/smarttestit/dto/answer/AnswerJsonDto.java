package de.ait.smarttestit.dto.answer;

import de.ait.smarttestit.models.Answer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "Answer json", description = "Representation of a answer")
public record AnswerJsonDto(@Positive
                            @Schema(description = "Answer_ID", example = "1")
                            Long id,

                            @NotBlank
                            @Schema(description = "Answer", example = "answer1")
                            String answerText) {

    /**
     * Converts an Answer object to an AnswerJsonDto object.
     *
     * @param answer the Answer object to be converted
     * @return the converted AnswerJsonDto object, or null if the input is null
     */
    public static AnswerJsonDto from(Answer answer) {
        if (answer == null) {
            return null;
        }
        return new AnswerJsonDto(
                answer.getId(),
                answer.getAnswerText());
    }

    /**
     * Converts a collection of Answer objects to a list of AnswerJsonDto objects.
     *
     * @param answers the collection of Answer objects to be converted
     * @return the list of converted AnswerJsonDto objects
     */
    public static List<AnswerJsonDto> from(Collection<Answer> answers) {
        return answers.stream()
                .map(AnswerJsonDto::from)
                .collect(Collectors.toList());
    }
}