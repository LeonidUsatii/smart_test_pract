package de.ait.smarttestit.dto.applicant;

import de.ait.smarttestit.models.Applicant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class ApplicantDto extends NewApplicantDto {

    @Positive
    @Schema(description = "user ID", example = "1")
    private Long id;

    public ApplicantDto(Long id, @NotBlank String firstName, @NotBlank String lastName, @NotBlank String email,
                        @NotBlank String hashPassword, String address, String phoneNumber) {
        super(firstName, lastName, email, hashPassword, address, phoneNumber);
        this.id = id;
    }

    public static ApplicantDto from(Applicant applicant) {

        return new ApplicantDto(
                applicant.getId(),
                applicant.getFirstName(),
                applicant.getLastName(),
                applicant.getEmail(),
                applicant.getHashPassword(),
                applicant.getAddress(),
                applicant.getPhoneNumber()
        );
    }
}
