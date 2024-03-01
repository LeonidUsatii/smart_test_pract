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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponse(responseCode = "401",
            description = "Teacher unauthorized",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardResponseDto.class)))

    @Tags(value = @Tag(name = "Answers"))
    @Schema(name = "Answer", description = "Answers with questions_id")

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
                @ApiResponse(responseCode = "403",
                        description = "Forbidden, only teacher available",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = StandardResponseDto.class)))

        })
        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping("/api/questions/{question_id}/answers")
        AnswerDto addAnswer(@PathVariable("question_id") Long questionId,
                                      @RequestBody NewAnswerDto newAnswer);

    @Operation(summary = "Receiving all answers", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnswerDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Answers not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden, only teacher available",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @GetMapping("/api/answers")
    List<AnswerDto> getAllAnswers();

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
                @ApiResponse(responseCode = "403",
                        description = "Forbidden, only teacher available",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = StandardResponseDto.class)))
        })

        @PutMapping("/api/questions/{question_id}/answers/{answer_id}")
        AnswerDto updateAnswer(@PathVariable("question_id") Long questionId,
                                         @PathVariable("answer_id") Long answerId,
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
                @ApiResponse(responseCode = "403",
                        description = "Forbidden, only teacher available",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = StandardResponseDto.class)))
        })
        @DeleteMapping("/api/questions/{question_id}/answers/{answer_id}")
        AnswerDto deleteAnswer(@PathVariable("question_id") Long courseId,
                                           @PathVariable("answer_id") Long lessonId);
    }

