package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.ApplicantTaskDto;
import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tags(@Tag(name = "Applicant"))
@Schema(name = "Applicant", description = "Applicant")
public interface ApplicantsApi {

    @Operation(summary = "Adding an applicant", description = "Available to admin ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Applicant added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicantTaskDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "An applicant already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),

    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/applicants")
    ApplicantDto addApplicant(NewApplicantTaskDto newApplicantDto);
}
