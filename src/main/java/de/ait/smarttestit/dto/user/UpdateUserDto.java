package de.ait.smarttestit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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

    @PositiveOrZero
    @Column(nullable = false)
    @Schema(description = " User level", example = "5")
    private int levelOfUser;
}
