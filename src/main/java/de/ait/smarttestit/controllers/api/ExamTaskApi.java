package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/examTask")

@Tags(value = {
        @Tag(name = "ExamTask")
})

@ApiResponse(responseCode = "403",
        description = "Forbidden", content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = StandardResponseDto.class)))

public interface ExamTaskApi {

    @Operation(summary = "Delete user", description = "Admin access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "User was successfully deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "User not found ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @DeleteMapping("/{examTask-id}")
    ResponseEntity<Void> deleteExamTask(@Parameter(description = "examTask ID", example = "1")
                                    @PathVariable("examTask-id") Long examTaskId);
}
