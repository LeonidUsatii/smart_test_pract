package de.ait.smarttestit.dto.token;

import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.Token;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class TokenDtoTest {

    @Test
    void testFrom() {
        // Test initialization
        Applicant testApplicant = new Applicant();
        testApplicant.setId(1L);

        Exam testExam = new Exam();
        testExam.setId(3L);

        Token testToken = new Token("12sdsds2324", LocalDateTime.parse("2024-04-04T12:30:00"), testApplicant, testExam);

        // Converting from Token model to TokenDto
        TokenDto tokenDto = TokenDto.from(testToken);

        // Assert values
        assertEquals("12sdsds2324", tokenDto.code());
        assertEquals(LocalDateTime.parse("2024-04-04T12:30:00"), tokenDto.expiryTime());
        assertEquals(1L, tokenDto.applicantId());
        assertEquals(3L, tokenDto.examId());
    }

    @Test
    void testFrom_whenExamIsNull() {
        // Test initialization
        Applicant testApplicant = new Applicant();
        testApplicant.setId(1L);

        Exam testExam = null;

        Token testToken = new Token("12sdsds2324", LocalDateTime.parse("2024-04-04T12:30:00"), testApplicant, testExam);

        // Converting from Token model to TokenDto
        TokenDto tokenDto = TokenDto.from(testToken);

        // Assert values
        assertEquals("12sdsds2324", tokenDto.code());
        assertEquals(LocalDateTime.parse("2024-04-04T12:30:00"), tokenDto.expiryTime());
        assertEquals(1L, tokenDto.applicantId());
        assertNull(tokenDto.examId());
    }

    @Test
    void testFrom_whenApplicantIsNull() {
        // Test initialization
        Applicant testApplicant = null;

        Exam testExam = new Exam();
        testExam.setId(3L);

        Token testToken = new Token("12sdsds2324", LocalDateTime.parse("2024-04-04T12:30:00"), testApplicant, testExam);

        // Converting from Token model to TokenDto
        TokenDto tokenDto = TokenDto.from(testToken);

        // Assert values
        assertEquals("12sdsds2324", tokenDto.code());
        assertEquals(LocalDateTime.parse("2024-04-04T12:30:00"), tokenDto.expiryTime());
        assertNull(tokenDto.applicantId());
        assertEquals(3L, tokenDto.examId());
    }

    @Test
    void testFrom_whenOtherFieldsIsNull() {
        // Test initialization
        Applicant testApplicant = new Applicant();
        testApplicant.setId(1L);

        Exam testExam = new Exam();
        testExam.setId(3L);

        Token testToken = new Token(null, null, testApplicant, testExam);

        // Converting from Token model to TokenDto
        TokenDto tokenDto = TokenDto.from(testToken);

        // Assert values
        assertNull(null, tokenDto.code());
        assertNull(null, String.valueOf(tokenDto.expiryTime()));
        assertEquals(1L, tokenDto.applicantId());
        assertEquals(3L, tokenDto.examId());
    }
}
