package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.dto.test_type.UpdateTestTypeDto;
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

@ApiResponses(value = {
        @ApiResponse(responseCode = "401",
                description = "Teacher unauthorized",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = StandardResponseDto.class))),
        @ApiResponse(responseCode = "403",
                description = "Forbidden, only teacher available",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = StandardResponseDto.class)))})
@Tags(value = @Tag(name = "TestTypes"))
@Schema(name = "TestType", description = "Test types")

public interface TestTypesApi {

    @Operation(summary = "Adding a test type", description = "Available to teacher ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Test type added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestTypeDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "A test type already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/testTypes")
    TestTypeDto addTestType(@RequestBody NewTestTypeDto newTestType);

    @Operation(summary = "Test type update", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateTestTypeDto.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Test type not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "The request was made incorrectly",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "A test type already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))
            )
    })
    @PutMapping("/api/testTypes/{testType_id}")
    TestTypeDto updateTestType(@PathVariable("testType_id") Long testTypeId,
                               @RequestBody @Valid UpdateTestTypeDto updateTestType);

    @Operation(summary = "Receiving all tests", description = "Available all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestTypeDto.class))),

    })
    @GetMapping("/api/testTypes")
    List<TestTypeDto> getAllTestTypes();

    @Operation(summary = "Delete TestType", description = "Available to teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestTypeDto.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "TestType not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @DeleteMapping("api/testTypes/{testType_id}")
    TestTypeDto deleteTestType(@PathVariable("testType_id") Long testTypeId);
}