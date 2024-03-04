package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.InfoAboutApplicantForExamDto;
import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.Token;
import de.ait.smarttestit.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Exam Preparation")
class ExamPreparationServiceUnitTests {

    @InjectMocks
    private ExamPreparationImpl examPreparationService;

    @Mock
    private TokenService tokenService;

    private Token token;

    private Applicant applicant;

    @BeforeEach
    void setUp() {

        applicant = new Applicant(1L, "Ivan", "Ivanov", "ivan@example.com", "123 Main St", "1234567890");

        Exam exam = new Exam();
        exam.setId(1L);

        token = new Token("validTokenCode", LocalDateTime.now().plusHours(1), applicant, exam);
    }

    @Test
    @DisplayName("Should return InfoAboutApplicantForExamDto when token is valid")
    void getInfoAboutApplicantForExamPositive() {
        when(tokenService.getByCodeOrThrow("validTokenCode")).thenReturn(token);

        InfoAboutApplicantForExamDto result = examPreparationService.getInfoAboutApplicantForExam("validTokenCode");

        assertNotNull(result);
        assertEquals(applicant.getFirstName(), result.getFirstName());
        assertEquals(applicant.getLastName(), result.getLastName());
        assertTrue(result.isTokenValid());
        assertEquals("Hello, Ivan Ivanov! You can begin the exam.", result.getMessageToApplicant());
        assertEquals(1L, result.getExamId());
    }

    @Test
    @DisplayName("Should throw an exception when token is not found")
    void getInfoAboutApplicantForExamNegative() {
        when(tokenService.getByCodeOrThrow("invalidTokenCode")).thenThrow(new RuntimeException("Token not found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> examPreparationService.getInfoAboutApplicantForExam("invalidTokenCode"),
                "Expected getByCodeOrThrow to throw, but it didn't");

        assertTrue(thrown.getMessage().contains("Token not found"));
    }
}
