package de.ait.smarttestit.dto.exam_task;

import de.ait.smarttestit.dto.test_type.TestTypeWithQuestionDto;
import de.ait.smarttestit.models.ExamTask;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "ExamTaskWithTestTypeDto")
public record ExamTaskWithTestTypeDto( @Schema(description = "TestTypeWithQuestionDto")
                                       List<TestTypeWithQuestionDto>testTypes,

                                       @Positive
                                       @Schema(description = "Exam id", example = "1")
                                       Long examId
) {
    public static ExamTaskWithTestTypeDto from(ExamTask examTask, Long examId) {
        List<TestTypeWithQuestionDto> testTypesList = examTask.getTestTypes().stream()
                .map(TestTypeWithQuestionDto::from)
                .collect(Collectors.toList());

        return new ExamTaskWithTestTypeDto(
                testTypesList,
                examId
        );
    }
}
