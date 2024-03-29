package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.exam.ExamDto;
import de.ait.smarttestit.dto.user.UserDto;
import de.ait.smarttestit.exceptions.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/exams")
@Tags(@Tag(name = "Exams"))
public interface ExamApi {

    @Operation(summary = "Get all exams list", description = "All exams access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request successfully processed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExamDto.class))),
    })
    @GetMapping
        //TODO add Pagination
    List<ExamDto> getListExams();

    @Operation(summary = "Get exam", description = "Get one ID exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request successfully processed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExamDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Exam not found ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestException.class)))
    })
    @GetMapping("/{exam-id}")
    ExamDto getExam(@Valid @Parameter(description = "Exam ID", example = "1")
                    @PathVariable("exam-id") @Min(value = 1,
            message = "Exam ID must be greater than or equal to 1") Long examId);

    @Operation(summary = "Delete exam", description = "Admin access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Exam was successfully deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Exam not found ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestException.class)))
    })
    @DeleteMapping("/{exam-id}")
    ResponseEntity<Void> deleteExam(@Valid @Parameter(description = "exam ID", example = "1")
                                    @PathVariable("exam-id") @Min(value = 1,
            message = "Exam ID must be greater than or equal to 1") Long examId);

    @Operation(summary = "Update information about exam", description = "Admin access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Information about exam was successfully updated ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExamDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PutMapping("/{exam-id}")
    ResponseEntity<ExamDto> updateExam(
            @Valid @Parameter(description = "exam ID", example = "1")
            @PathVariable("exam-id") @Min(value = 1,
                    message = "Exam ID must be greater than or equal to 1") Long examId,
            @Parameter(description = "Exam grade", example = "70")
            @RequestParam(value = "examScore") @Min(value = 0,
                    message = "Exam score must be greater than or equal to 0") int examScore,

            @Parameter(description = "Exam start time", example = "2023-02-02T12:00")
            @RequestParam(value = "testStartTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime examStartTime,

            @Parameter(description = "Exam end time", example = "2024-02-02T13:00")
            @RequestParam(value = "examEndTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime examEndTime,

            @Parameter(description = "Exam execution time", example = "120")
            @RequestParam(value = "examDuration") int examDuration,

            @Parameter(description = "Exam status", example = "PLANNED")
            @RequestParam(value = "examStatus") String examStatus,

            @Parameter(description = "User ID", example = "4")
            @RequestParam(value = "userId", required = false) Long userId,

            @Parameter(description = "Applicant ID associated with the exam")
            @RequestParam(value = "applicantId", required = false) Long applicantId,

            @Parameter(description = "Test ID", example = "5")
            @RequestParam(value = "examTaskId") @Min(value = 1,
                    message = "Exam task ID must be greater than or equal to 1") Long examTaskId);
}

