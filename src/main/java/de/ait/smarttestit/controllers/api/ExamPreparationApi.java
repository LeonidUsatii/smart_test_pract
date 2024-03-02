package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.applicant.InfoAboutApplicantForExamDto;
import de.ait.smarttestit.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/examPreparation")

@Tags(value = {
        @Tag(name = "ExamPreparation")
})

@ApiResponse(responseCode = "403",
        description = "Forbidden", content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = StandardResponseDto.class)))

public interface ExamPreparationApi {
    @Operation(summary = "Get applicant info", description = "All  access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request successfully processed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Clinic not found ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })

    @GetMapping("/{token-code}")
    InfoAboutApplicantForExamDto examPreparation(@Parameter(description = "token Code", example = "qwerty")
                                                 @PathVariable("token-code") String tokenCode);
}
