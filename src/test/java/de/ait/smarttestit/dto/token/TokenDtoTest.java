package de.ait.smarttestit.dto.token;

import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.Token;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


class TokenDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testFrom() {

        Applicant testApplicant = new Applicant();
        testApplicant.setId(1L);

        Exam testExam = new Exam();
        testExam.setId(3L);

        Token testToken = new Token("12sdsds2324", LocalDateTime.parse("2024-04-04T12:30:00"), testApplicant, testExam);

        TokenDto tokenDto = TokenDto.from(testToken);

        assertEquals("12sdsds2324", tokenDto.code());
        assertEquals(LocalDateTime.parse("2024-04-04T12:30:00"), tokenDto.expiryTime());
        assertEquals(1L, tokenDto.applicantId());
        assertEquals(3L, tokenDto.examId());
    }

    @Test
    void testFrom_whenExamIsNull() {

        Applicant testApplicant = new Applicant();
        testApplicant.setId(1L);

        Exam testExam = null;

        Token testToken = new Token("12sdsds2324", LocalDateTime.parse("2024-04-04T12:30:00"), testApplicant, testExam);

        TokenDto tokenDto = TokenDto.from(testToken);

        assertEquals("12sdsds2324", tokenDto.code());
        assertEquals(LocalDateTime.parse("2024-04-04T12:30:00"), tokenDto.expiryTime());
        assertEquals(1L, tokenDto.applicantId());
        assertNull(tokenDto.examId());
    }

    @Test
    void testFrom_whenApplicantIsNull() {

        Applicant testApplicant = null;

        Exam testExam = new Exam();
        testExam.setId(3L);

        Token testToken = new Token("12sdsds2324", LocalDateTime.parse("2024-04-04T12:30:00"), testApplicant, testExam);

        TokenDto tokenDto = TokenDto.from(testToken);

        assertEquals("12sdsds2324", tokenDto.code());
        assertEquals(LocalDateTime.parse("2024-04-04T12:30:00"), tokenDto.expiryTime());
        assertNull(tokenDto.applicantId());
        assertEquals(3L, tokenDto.examId());
    }

    @Test
    void testFrom_whenOtherFieldsIsNull() {

        Applicant testApplicant = new Applicant();
        testApplicant.setId(1L);

        Exam testExam = new Exam();
        testExam.setId(3L);

        Token testToken = new Token(null, null, testApplicant, testExam);

        TokenDto tokenDto = TokenDto.from(testToken);

        assertNull(null, tokenDto.code());
        assertNull(null, String.valueOf(tokenDto.expiryTime()));
        assertEquals(1L, tokenDto.applicantId());
        assertEquals(3L, tokenDto.examId());
    }

    @Test
    void shouldBeInvalidWithBlankCode() {

        Token token = new Token("", LocalDateTime.now().plusDays(1), new Applicant(), new Exam());

        TokenDto dto = TokenDto.from(token);
        Set<ConstraintViolation<TokenDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Violations should be present for a blank code");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("The id must not be null")), "There should be a violation for blank code");
    }

    @Test
    void shouldBeInvalidWithNonPositiveApplicantId() {

        TokenDto dtoZero = new TokenDto("12sdsds2324", LocalDateTime.now().plusDays(1), 0L, 2L);
        Set<ConstraintViolation<TokenDto>> violationsZero = validator.validate(dtoZero);
        assertFalse(violationsZero.isEmpty(), "Violations should be present for a zero applicantId");
        assertTrue(
                violationsZero.stream().anyMatch(v -> v.getMessage().contains("The id must be positive")),
                "There should be a violation message indicating the applicantId must be greater than 0 for zero value"
        );

        TokenDto dtoNegative = new TokenDto("12sdsds2324", LocalDateTime.now().plusDays(1), -1L, 2L);
        Set<ConstraintViolation<TokenDto>> violationsNegative = validator.validate(dtoNegative);
        assertFalse(violationsNegative.isEmpty(), "Violations should be present for a negative applicantId");
        assertTrue(
                violationsNegative.stream().anyMatch(v -> v.getMessage().contains("The id must be positive")),
                "There should be a violation message indicating the applicantId must be greater than 0 for negative value"
        );
    }
}
