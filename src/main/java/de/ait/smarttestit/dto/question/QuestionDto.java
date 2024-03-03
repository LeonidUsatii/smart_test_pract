package de.ait.smarttestit.dto.question;

import de.ait.smarttestit.models.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "Question", description = "Representation of a question")
public record QuestionDto(@NotNull(message = "The id must not be null")
                          @Positive(message = "The id must be positive")
                          @Schema(description = "Question id", example = "1")
                          Long id,

                          @Schema(description = "Question", example = "What is an interface in Java?")
                          @NotBlank
                          String questionText,

                          @Positive
                          @Schema(description = "Level of question", example = "1")
                          int level,

                          @Positive
                          @Schema(description = "Test type id", example = "5")
                          Long testTypeId) {

    public static  QuestionDto from(Question question) {
        if (question == null) {
            return null;
        }
        return new QuestionDto(
                question.getId(),
                question.getQuestionText(),
                question.getLevel(),
                question.getTestType().getId());
    }
    public static List<QuestionDto> from(Collection<Question> questions){
        return questions.stream()
                .map(QuestionDto::from)
                .collect(Collectors.toList());
    }
}