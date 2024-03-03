package de.ait.smarttestit.dto.exam;

import de.ait.smarttestit.models.Exam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "Exam", description = "Representation of an exam")
public record ExamDto(@Schema(description = "Unique identifier of the exam", example = "1")
                      @NotNull(message = "The id must not be null")
                      @Positive(message = "The id must be positive")
                      Long id,

                      @Schema(description = "Score of the exam", example = "85")
                      @PositiveOrZero(message = "Exam score must be zero or a positive number")
                      int examScore,

                      @Schema(description = "Start time of the exam", example = "2024-01-01T09:00:00", required = true)
                      @NotBlank(message = "Exam start time must not be blank")
                      String examStartTime,

                      @Schema(description = "End time of the exam", example = "2024-01-01T12:00:00", required = true)
                      @NotBlank(message = "Exam end time must not be blank")
                      String examEndTime,

                      @Schema(description = "Duration of the exam in minutes", example = "180")
                      @Positive(message = "Exam duration must be a positive number")
                      int examDuration,

                      @Schema(description = "Status of the exam", example = "COMPLETED")
                      @NotBlank(message = "Exam status must not be blank")
                      String examStatus,

                      @Schema(description = "User ID associated with the exam", example = "2")
                      Long userId,

                      @Schema(description = "Applicant ID associated with the exam", example = "2")
                      Long applicantId,

                      @Schema(description = "Test ID associated with the exam", example = "3")
                      @Positive(message = "Exam task ID must be a positive number")
                      Long examTaskId) {

    public static ExamDto from(Exam exam) {
        if (exam == null) {
            return null;
        }
        return new ExamDto(
                exam.getId(),
                exam.getExamScore(),
                exam.getExamStartTime().toString(),
                exam.getExamEndTime().toString(),
                exam.getExamDuration(),
                exam.getExamStatus().toString(),
                exam.getUser().getId(),
                null,
                exam.getExamTask().getId());
    }
}
