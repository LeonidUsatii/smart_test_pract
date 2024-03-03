package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.exam.NewFinishExamDto;
import de.ait.smarttestit.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/finishExam")

@Tags(value = {
        @Tag(name = "FinishExam")
})

@ApiResponse(responseCode = "403",
        description = "Forbidden", content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = StandardResponseDto.class)))

public interface FinishExamApi {
    @Operation(summary = "Update information about exam", description = "Admin access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Information about user was successfully updated ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))

    })

    @PutMapping
    String finishExam(@RequestBody NewFinishExamDto newFinishExamDto);
}
