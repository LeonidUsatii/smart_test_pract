package de.ait.smarttestit.dto.token;

import de.ait.smarttestit.models.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "Token", description = "Representation of a token")
public record TokenDto(@Schema(description = "Tokens code", example = "12sdsds2324", required = true)
                       @NotBlank
                       String code,

                       @Schema(description = "Code validity period", example = "2024-04-04T12:30:00")
                       LocalDateTime expiryTime,

                       @Schema(description = "Applicant identifications code", example = "1")
                       @Positive
                       Long applicantId,

                       @Schema(description = "Exam identifications code", example = "2")
                       Long examId) {

    /**
     * Convert a Token object to a TokenDto object.
     *
     * @param token The Token object to convert.
     * @return The converted TokenDto object, or null if the input token is null.
     */
    public static TokenDto from(Token token) {
        if (token == null) {
            return null;
        }

        String code = token.getCode();
        LocalDateTime expiryTime = token.getExpiredDateTime();
        Long applicantId = token.getApplicant() == null ? null : token.getApplicant().getId();
        Long examId = token.getExam() == null ? null : token.getExam().getId();

        return new TokenDto(code, expiryTime, applicantId, examId);
    }

    /**
     * Converts a collection of Token objects to a collection of TokenDto objects.
     *
     * @param tests The collection of Token objects to convert.
     * @return The converted collection of TokenDto objects.
     */
    public static List<TokenDto> from(Collection<Token> tests) {
        return tests.stream()
                .map(TokenDto::from)
                .collect(Collectors.toList());
    }
}
