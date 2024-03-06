package de.ait.smarttestit.dto.mail;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmailRequestDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Validation EmailRequestDto")
    class ValidationEmailRequestDto {
        @Test
        void whenEmailValid_thenNoConstraintViolations() {
            EmailRequestDto emailRequestDto = new EmailRequestDto("user@example.com", "Test Subject", "Test text");
            Set<ConstraintViolation<EmailRequestDto>> violations = validator.validate(emailRequestDto);
            assertTrue(violations.isEmpty());
        }

        @Test
        void whenEmailInvalid_thenConstraintViolation() {
            EmailRequestDto emailRequestDto = new EmailRequestDto("invalid-email", "Test Subject", "Test text");
            Set<ConstraintViolation<EmailRequestDto>> violations = validator.validate(emailRequestDto);
            assertEquals(1, violations.size());
        }

        @Test
        void whenSubjectBlank_thenConstraintViolation() {
            EmailRequestDto emailRequestDto = new EmailRequestDto("user@example.com", "", "Test text");
            Set<ConstraintViolation<EmailRequestDto>> violations = validator.validate(emailRequestDto);
            assertEquals(1, violations.size());
        }

        @Test
        void whenTextBlank_thenConstraintViolation() {
            EmailRequestDto emailRequestDto = new EmailRequestDto("user@example.com", "Test Subject", "");
            Set<ConstraintViolation<EmailRequestDto>> violations = validator.validate(emailRequestDto);
            assertEquals(1, violations.size());
        }
    }

    @Nested
    @DisplayName("Method Validation IsValid")
    class MethodValidationIsValid {

        @Test
        void whenValidEmailRequest_thenIsValidReturnsTrue() {

            EmailRequestDto validEmailRequest = new EmailRequestDto("user@example.com", "Your Subject", "Hello, this is the email content.");

            assertTrue(EmailRequestDto.isValid(validEmailRequest), "The email request should be valid");
        }

        @Test
        void whenEmailRequestWithBlankEmail_thenIsValidReturnsFalse() {

            EmailRequestDto invalidEmailRequest = new EmailRequestDto("", "Your Subject", "Hello, this is the email content.");

            assertFalse(EmailRequestDto.isValid(invalidEmailRequest), "The email request with blank email should be invalid");
        }

        @Test
        void whenEmailRequestWithNullFields_thenIsValidReturnsFalse() {

            EmailRequestDto nullEmailRequest = new EmailRequestDto(null, null, null);

            assertFalse(EmailRequestDto.isValid(nullEmailRequest), "The email request with null fields should be invalid");
        }

        @Test
        void whenEmailRequestIsNullOrBlank_thenIsValidReturnsFalse() {

            assertFalse(EmailRequestDto.isValid(null), "The method should return false for null input");

            EmailRequestDto blankEmailRequest = new EmailRequestDto("  ", "  ", "  ");

            assertFalse(EmailRequestDto.isValid(blankEmailRequest), "The email request with all blank fields should be invalid");
        }
    }
}
