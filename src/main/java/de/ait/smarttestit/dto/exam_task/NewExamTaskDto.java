package de.ait.smarttestit.dto.exam_task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "NewExamTaskDto", description = "Data Transfer Object for creating a new exam task")
public record NewExamTaskDto(@Schema(description = "Title of the test", example = "Final Exam", required = true)
                             @NotBlank(message = "Test title is required")
                             @Size(min = 1, max = 255, message = "Test title must be between 1 and 255 characters")
                             String examTaskTitle) {
}
