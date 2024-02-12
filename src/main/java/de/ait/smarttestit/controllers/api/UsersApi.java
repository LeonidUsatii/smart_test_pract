package de.ait.smarttestit.controllers.api;

import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.user.NewUserDto;
import de.ait.smarttestit.dto.user.UpdateUserDto;
import de.ait.smarttestit.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")

@Tags(value = {
        @Tag(name = "Users")
})

@ApiResponse(responseCode = "403",
        description = "Forbidden", content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = StandardResponseDto.class)))

public interface UsersApi {

    @Operation(summary = "Create user", description = "Admin access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403",
                    description = "Forbidden", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "201", description = "Successfully added the user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "User with email already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - The request could not be understood due to malformed syntax.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    UserDto addUser(@RequestBody NewUserDto newUser);

    @Operation(summary = "Get users list", description = "All users access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request successfully processed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
    })
    @GetMapping
    List<UserDto> getListUsers();

    @Operation(summary = "Get user", description = "All users access")
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
    @GetMapping("/{user-id}")
    UserDto getUser(@Parameter(description = "user ID", example = "1")
                        @PathVariable("user-id") Long userId);

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
    @DeleteMapping("/{user-id}")
    ResponseEntity<Void> deleteUser(@Parameter(description = "user ID", example = "1")
                           @PathVariable("user-id") Long userId);

    @Operation(summary = "Update information about user", description = "Admin access")
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
    @PutMapping("/{user-id}")
    ResponseEntity<UserDto> updateUser(@Parameter(description = "user ID", example = "1")
                           @PathVariable("user-id") Long userId,
                                       @RequestBody UpdateUserDto updateUser);
}


