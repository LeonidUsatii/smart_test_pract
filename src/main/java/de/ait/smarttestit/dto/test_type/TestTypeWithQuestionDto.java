package de.ait.smarttestit.dto.test_type;

import de.ait.smarttestit.dto.question.QuestionWithAnswersDto;
import de.ait.smarttestit.models.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "TestTypeWithQuestionDto")
public record TestTypeWithQuestionDto(@NotBlank
                                      @Schema(description = "TestType title", example = "Logic")
                                      String name,

                                      @Schema(description = "List of questions")
                                      List<QuestionWithAnswersDto> questions
) {
    public static TestTypeWithQuestionDto from(TestType testType) {
        List<QuestionWithAnswersDto> questions = testType.getQuestions().stream()
                .map(QuestionWithAnswersDto::from)
                .collect(Collectors.toList());
        return new TestTypeWithQuestionDto(
                testType.getName(),
                questions
        );
    }
}
