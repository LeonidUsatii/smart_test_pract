package de.ait.smarttestit.dto.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "EmailRequest", description = "Request data for sending email")
public record EmailRequestDto(@NotBlank(message = "Recipient email cannot be blank")
                              @Email(message = "Recipient email must be a valid email address")
                              @Schema(description = "The email address of the recipient", example = "user@example.com")
                              String to,

                              @NotBlank(message = "Email subject cannot be blank")
                              @Schema(description = "The subject of the email", example = "Your Subject")
                              String subject,

                              @NotBlank(message = "Email text cannot be blank")
                              @Schema(description = "The text content of the email", example = "Hello, this is the email content.")
                              String text){
    public static boolean isValid(EmailRequestDto dto) {
        if (dto == null) {
            return false;
        }

        return dto.to() != null && !dto.to().isBlank() &&
                dto.subject() != null && !dto.subject().isBlank() &&
                dto.text() != null && !dto.text().isBlank();
    }
}
