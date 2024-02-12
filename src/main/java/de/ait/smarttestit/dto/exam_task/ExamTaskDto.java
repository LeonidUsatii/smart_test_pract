package de.ait.smarttestit.dto.exam_task;

import de.ait.smarttestit.models.ExamTask;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(name = "ExamTaskDto", description = "DTO for exam task details")
public record ExamTaskDto(@Schema(description = "Unique identifier of the exam task", example = "1", required = true)
                          @Positive(message = "ID is required")
                          Long id,

                          @Schema(description = "Title of the test", example = "Math Final Exam", required = true)
                          @NotBlank(message = "Test title is required")
                          @Size(min = 1, max = 255, message = "Test title must be between 1 and 255 characters")
                          String examTaskTitle) {

    public static ExamTaskDto from(ExamTask examTask) {

        return new ExamTaskDto(
                examTask.getId(),
                examTask.getExamTaskTitle()
        );
    }
}
