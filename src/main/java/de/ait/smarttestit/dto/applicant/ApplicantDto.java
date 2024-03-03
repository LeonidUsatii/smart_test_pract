package de.ait.smarttestit.dto.applicant;

import de.ait.smarttestit.models.Applicant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ApplicantDto extends NewApplicantDto {

    @NotNull(message = "The id must not be null")
    @Positive(message = "The id must be positive")
    @Schema(description = "user ID", example = "1")
    private Long id;

    public ApplicantDto(Long id,
                        @NotBlank
                        String firstName,
                        @NotBlank
                        String lastName,
                        @NotBlank
                        String email,
                        String address,
                        String phoneNumber) {
        super(firstName, lastName, email, address, phoneNumber);
        this.id = id;
    }


    /**
     * Converts an Applicant object to an ApplicantDto object.
     *
     * @param applicant the Applicant object to convert
     * @return the converted ApplicantDto object
     */
    public static ApplicantDto from(Applicant applicant) {
        if (applicant == null) {
            return null;
        }
        return new ApplicantDto(
                applicant.getId(),
                applicant.getFirstName(),
                applicant.getLastName(),
                applicant.getEmail(),
                applicant.getAddress(),
                applicant.getPhoneNumber()
        );
    }
}
