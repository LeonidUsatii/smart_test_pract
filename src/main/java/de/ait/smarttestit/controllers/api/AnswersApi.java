package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.answer.AnswerDto;
import de.ait.smarttestit.dto.answer.NewAnswerDto;
import de.ait.smarttestit.dto.answer.UpdateAnswerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Tags(@Tag(name = "Answers"))
@RequestMapping("/api/questions")
public interface AnswersApi {

    @Operation(summary = "Adding a answer", description = "Available to teacher ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Answer added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnswerDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Question not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "An answer in this question already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),

    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{question_id}/answers")
    AnswerDto addAnswer(@PathVariable("question_id") @Min(value = 1,
            message = "Question ID must be greater than 0") Long questionId,
                        @RequestBody @Valid NewAnswerDto newAnswer);

    @Operation(summary = "Answer update", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateAnswerDto.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Question or answer not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "The request was made incorrectly",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "An answer in this question already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
    })

    @PutMapping("/{question_id}/answers/{answer_id}")
    AnswerDto updateAnswer(@PathVariable("question_id") @Min(value = 1, message = "Question ID must be greater than 0") Long questionId,
                           @PathVariable("answer_id") @Min(value = 1, message = "Answer ID must be greater than 0") Long answerId,
                           @RequestBody @Valid UpdateAnswerDto updateAnswer);

    @Operation(summary = "Delete Answer", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnswerDto.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Question or answer not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
    })
    @DeleteMapping("/{question_id}/answers/{answer_id}")
    AnswerDto deleteAnswer(@PathVariable("question_id") @Min(value = 1, message = "Question ID must be greater than 0") Long courseId,
                           @PathVariable("answer_id") @Min(value = 1, message = "Answer ID must be greater than 0") Long lessonId);

    @Operation(summary = "Receiving all answers", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnswerDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Answers not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
    })
    @GetMapping("/answers")
    List<AnswerDto> getAllAnswers();
}

