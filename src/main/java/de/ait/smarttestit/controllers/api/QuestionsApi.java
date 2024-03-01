package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.question.NewQuestionDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.dto.question.UpdateQuestionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponse(responseCode = "401",
        description = "Teacher unauthorized",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StandardResponseDto.class)))

@Tags(value = @Tag(name = "Questions"))
@Schema(name = "Question", description = "Questions")

public interface QuestionsApi {

    @Operation(summary = "Adding a question", description = "Available to teacher ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Question not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "A question in this test already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden, only teacher available",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))

    })
    @PostMapping("/api/testTypes/{testType_id}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    QuestionDto addQuestion(@PathVariable("testType_id") Long testId,
                                      @RequestBody @Valid NewQuestionDto newQuestion);

    @Operation(summary = "Receiving all questions", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Questions not found ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden, only teacher available",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @GetMapping("/api/questions")
    List<QuestionDto> getAllQuestions();

    @Operation(summary = "Question update", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateQuestionDto.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Question not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "The request was made incorrectly",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "A question in this test already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden, only teacher available",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })

    @PutMapping("/api/testTypes/{testType_id}/questions/{question_id}")
    QuestionDto updateQuestion(@PathVariable("testType_id") Long testTypeId,
                                         @PathVariable("question_id") Long questionId,
                                         @RequestBody @Valid UpdateQuestionDto updateQuestion);

    @Operation(summary = "Delete Question", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionDto.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Question not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden, only teacher available",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @DeleteMapping("/api/testTypes/{testType_id}/questions/{question_id}")
    QuestionDto deleteQuestion(@PathVariable("question_id") Long questionId);
}