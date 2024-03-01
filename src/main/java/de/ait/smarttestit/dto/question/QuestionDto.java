package de.ait.smarttestit.dto.question;

import de.ait.smarttestit.models.Answer;
import de.ait.smarttestit.models.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "Question", description = "Representation of a question")
public record QuestionDto(@Positive
                          @Schema(description = "Question id", example = "1")
                          Long id,

                          @NotBlank
                          @Schema(description = "Question", example = "What is an interface in Java?")
                          String questionText,

                          @Positive
                          @Schema(description = "Level of question", example = "1")
                          int level,

                          @Positive
                          @Schema(description = "Test type id", example = "5")
                          Long testTypeId,

                          @Schema(description = "List of answers", example = """
                                  {
                                    "answerId": 2,
                                    "answerText": "answerId",
                                    "isCorrect": true,
                                    "QuestionId": 2
                                  }""")
                          List<Answer> answers) {

    /**
     * Converts a Question object to a QuestionDto object.
     *
     * @param question the Question object to convert
     * @return the converted QuestionDto object
     */
    public static QuestionDto from(Question question) {
        if (question == null) {
            return null;
        }
        return new QuestionDto(
                question.getId(),
                question.getQuestionText(),
                question.getLevel(),
                question.getTestType() == null ? null : question.getTestType().getId(),
                question.getAnswers());
    }

    /**
     * Converts a collection of Question objects into a list of QuestionDto objects.
     *
     * @param questions a collection of Question objects to be converted
     * @return a list of QuestionDto objects representing the converted collection
     */
    public static List<QuestionDto> from(Collection<Question> questions) {
        return questions.stream()
                .map(QuestionDto::from)
                .collect(Collectors.toList());
    }
}
