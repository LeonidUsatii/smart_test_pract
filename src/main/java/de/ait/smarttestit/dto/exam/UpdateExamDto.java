package de.ait.smarttestit.dto.exam;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(name = "UpdateExamDto " , description = "Adding a update exam")
public record UpdateExamDto(@Schema(description = "Exam grade", example = "70", required = true)
                            @PositiveOrZero
                            int examScore,

                            @Schema(description = "Test start time", example = "2023-02-02T12:00", required = true)
                            @NotNull
                            LocalDateTime examStartTime,

                            @Schema(description = "Test end time", example = "2023-02-02T13:30", required = true)
                            LocalDateTime examEndTime,

                            @Schema(description = "Test execution time in minutes", example = "90", required = true)
                            @Positive
                            int examDuration,

                            @Schema(description = "Exam status", example = "PLANNED", required = true)
                            @NotBlank
                            String examStatus,

                            @Schema(description = "User ID", example = "4", required = true)
                            Long userId,

                            @Schema(description = "Applicant ID associated with the exam", example = "2", required = true)
                            Long applicantId,

                            @Schema(description = "Test ID", example = "5", required = true)
                            @Positive
                            Long examTaskId) {
}
