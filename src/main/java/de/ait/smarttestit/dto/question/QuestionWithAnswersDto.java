package de.ait.smarttestit.dto.question;

import de.ait.smarttestit.dto.answer.AnswerJsonDto;
import de.ait.smarttestit.models.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "Question with answers", description = "Representation of a question with answers")
public record QuestionWithAnswersDto(@NotNull(message = "The id must not be null")
                                     @Positive(message = "The id must be positive")
                                     @Schema(description = "Question id", example = "1")
                                     Long id,

                                     @NotBlank
                                     @Schema(description = "Question", example = "What is an interface in Java?")
                                     String questionText,

                                     @Schema(description = "List of answers")
                                     List<AnswerJsonDto> answers) {

    public static QuestionWithAnswersDto from(Question question) {
        if (question == null) {
            return null;
        }
        List<AnswerJsonDto> answerDtos = question.getAnswers().stream()
                .map(AnswerJsonDto::from)
                .collect(Collectors.toList());

        return new QuestionWithAnswersDto(
                question.getId(),
                question.getQuestionText(),
                answerDtos);
    }

    public static List<QuestionWithAnswersDto> from(Collection<Question> questions){
        return questions.stream()
                .map(QuestionWithAnswersDto::from)
                .collect(Collectors.toList());
    }
}