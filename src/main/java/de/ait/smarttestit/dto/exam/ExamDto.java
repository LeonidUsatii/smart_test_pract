package de.ait.smarttestit.dto.exam;

import de.ait.smarttestit.models.Exam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "Exam", description = "Representation of an exam")
public record ExamDto(@Schema(description = "Unique identifier of the exam", example = "1", required = true) @Positive Long id,
                      @Schema(description = "Score of the exam", example = "85", required = true) @PositiveOrZero int examScore,
                      @Schema(description = "Start time of the exam", example = "2024-01-01T09:00:00", required = true) @NotBlank String examStartTime,
                      @Schema(description = "End time of the exam", example = "2024-01-01T12:00:00", required = true) @NotBlank String examEndTime,
                      @Schema(description = "Duration of the exam in minutes", example = "180", required = true) @Positive int examDuration,
                      @Schema(description = "Status of the exam", example = "COMPLETED", required = true) @NotBlank String examStatus,
                      @Schema(description = "User ID associated with the exam", example = "2", required = true) Long userId,
                      @Schema(description = "Applicant ID associated with the exam", example = "2", required = true) Long applicantId,
                      @Schema(description = "Test ID associated with the exam", example = "3", required = true) @Positive Long examTaskId) {

    public static ExamDto from(Exam exam) {

        if (exam.getUser() == null) {
            return new ExamDto(
                    exam.getId(),
                    exam.getExamScore(),
                    exam.getExamStartTime().toString(),
                    exam.getExamEndTime().toString(),
                    exam.getExamDuration(),
                    exam.getExamStatus().toString(),
                    null,
                    exam.getApplicant().getId(),
                    exam.getExamTask().getId());

        } else {
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
}
