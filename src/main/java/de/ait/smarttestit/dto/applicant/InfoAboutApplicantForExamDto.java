package de.ait.smarttestit.dto.applicant;

import de.ait.smarttestit.models.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoAboutApplicantForExamDto {

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "Applicant Name", example = "Ivan")
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "Applicant's last name", example = "Ivanov")
    private String lastName;

    @Schema(description = "token validity information", example = "True")
    private boolean isTokenValid;

    @NotBlank
    @Schema(description = "Notifying the applicant that the exam is ready to begin", example = "Hello Ivan Examenov! You can start the exam.")
    private String messageToApplicant;

    @Positive
    @Schema(description = "exam ID", example = "1")
    private Long examId;

    public static InfoAboutApplicantForExamDto from(Token token) {

        return new InfoAboutApplicantForExamDto(
                token.getApplicant().getFirstName(),
                token.getApplicant().getLastName(),
                (token.getExpiredDateTime().isAfter(LocalDateTime.now())),
                "Hello," + " "  + token.getApplicant().getFirstName() + " " + token.getApplicant().getLastName() + "! "
                        + ((token.getExpiredDateTime().isAfter(LocalDateTime.now())) ? "You can begin the exam." : "You need to update the link."),
                token.getExam().getId()
        );
    }
}
