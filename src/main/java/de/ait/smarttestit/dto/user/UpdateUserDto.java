package de.ait.smarttestit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "User Name", example = "Ivan")
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "User's last name", example = "Ivanov")
    private String lastName;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Schema(description = "User email", example = "simple@mail.com")
    private String email;

    @NotBlank
    @Schema(description = "User password", example = "qwerty007!")
    private String hashPassword;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Schema(description = "User role", example = "USER")
    private String userRole;

    @PositiveOrZero
    @Column(nullable = false)
    @Schema(description = " User level", example = "5")
    private int levelOfUser;
}
