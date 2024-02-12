package de.ait.smarttestit.dto.exam_task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateExamTaskDto", description = "DTO for updating an existing exam task")
public record UpdateExamTaskDto(@Schema(description = "New title of the test, if updating", example = "Updated Math Final Exam")
                                @Size(min = 1, max = 255, message = "Test title must be between 1 and 255 characters")
                                String examTaskTitle) {
}