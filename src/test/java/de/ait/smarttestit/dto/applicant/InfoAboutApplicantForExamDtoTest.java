package de.ait.smarttestit.dto.applicant;

import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.Token;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InfoAboutApplicantForExamDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Positive test: a valid token")
    void shouldCreateDtoFromValidToken() {

        Token token = new Token();
        token.setCode("validTokenCode");
        token.setExpiredDateTime(LocalDateTime.now().plusDays(1));
        Applicant applicant = new Applicant();
        applicant.setFirstName("Ivan");
        applicant.setLastName("Ivanov");
        token.setApplicant(applicant);
        Exam exam = new Exam();
        exam.setId(1L);
        token.setExam(exam);

        InfoAboutApplicantForExamDto dto = InfoAboutApplicantForExamDto.from(token);

        assertNotNull(dto);
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Ivanov", dto.getLastName());
        assertTrue(dto.isTokenValid(), "The token must be valid.");
        assertEquals("Hello, Ivan Ivanov! You can begin the exam.", dto.getMessageToApplicant());
        assertEquals(Long.valueOf(1), dto.getExamId());
    }

    @Test
    @DisplayName("Negative test: expired token")
    void shouldCreateDtoFromExpiredToken() {

        Token token = new Token();
        token.setCode("expiredTokenCode");
        token.setExpiredDateTime(LocalDateTime.now().minusDays(1));
        Applicant applicant = new Applicant();
        applicant.setFirstName("Ivan");
        applicant.setLastName("Ivanov");
        token.setApplicant(applicant);
        Exam exam = new Exam();
        exam.setId(2L);
        token.setExam(exam);

        InfoAboutApplicantForExamDto dto = InfoAboutApplicantForExamDto.from(token);

        assertNotNull(dto);
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Ivanov", dto.getLastName());
        assertFalse(dto.isTokenValid());
        assertEquals("Hello, Ivan Ivanov! You need to update the link.", dto.getMessageToApplicant());
        assertEquals(Long.valueOf(2), dto.getExamId());
    }

    @Test
    @DisplayName("Validation test: incorrect DTO data")
    void dtoValidationShouldDetectInvalidFields() {

        InfoAboutApplicantForExamDto dto = new InfoAboutApplicantForExamDto(
                "",
                "",
                true,
                "",
                -1L
        );

        Set<ConstraintViolation<InfoAboutApplicantForExamDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "There must be irregularities in validating incorrect data");
        assertEquals(4, violations.size(), "Four validation breaches are expected");
    }
}
