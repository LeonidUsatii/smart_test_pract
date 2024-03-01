package de.ait.smarttestit.dto.applicant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateApplicantDto {

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

    @Column(nullable = true)
    @Schema(description = "Applicant address", example = "Unter den Linden 5, 10117 Berlin, Germany")
    private String address;

    @Column(nullable = true)
    @Schema(description = "Applicant phone number", example = "+49 30 1234567")
    private String phoneNumber;
}
