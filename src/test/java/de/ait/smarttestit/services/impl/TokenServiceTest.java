package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.dto.exam_task.NewTestsParamDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.*;
import de.ait.smarttestit.repositories.ApplicantRepository;
import de.ait.smarttestit.repositories.TokenRepository;
import de.ait.smarttestit.services.ApplicantService;
import de.ait.smarttestit.services.ExamService;
import de.ait.smarttestit.services.ExamTasksService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class TokenServiceTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private ExamService examService;

    @Mock
    private ApplicantService applicantService;

    @Mock
    private ExamTasksService examTasksService;

    @Nested
    @DisplayName("Token generation")
    class generateApplicantToken {

        @Test
        public void generateApplicantToken_PositiveTest() {

            Long examId = 1L;
            Applicant applicant = new Applicant();
            Exam exam = new Exam();

            when(examService.getExamOrThrow(examId)).thenReturn(exam);
            when(tokenRepository.save(any(Token.class))).thenReturn(new Token("generatedToken", LocalDateTime.now(), applicant, exam));

            String resultToken = tokenService.generateApplicantToken(examId, applicant);

            verify(examService, times(1)).getExamOrThrow(examId);
            verify(tokenRepository, times(1)).save(any(Token.class));

            assertNotEquals("", resultToken);
        }

        @Test
        public void generateApplicantToken_WhenExamNotFound() {

            Long examId = 1L;
            Applicant applicant = new Applicant();

            when(examService.getExamOrThrow(examId)).thenReturn(null);

            String resultToken = tokenService.generateApplicantToken(examId, applicant);

            verify(examService, times(1)).getExamOrThrow(examId);
            verify(tokenRepository, never()).save(any(Token.class));

            assertEquals("", resultToken);
        }

        @Test
        public void testGenerateApplicantToken_Success() {

            NewApplicantDto applicantInfo = new NewApplicantDto("Ivan", "Ivanov", "ivanov@gmail.com", "Berlin", "0123456789");
            NewTestsParamDto newTestsParamDto = new NewTestsParamDto(1L, 2, 10);
            List<NewTestsParamDto> examTaskDtoList = Collections.singletonList(newTestsParamDto);
            NewApplicantTaskDto applicantTaskDto = new NewApplicantTaskDto("Exam1", 30, examTaskDtoList, applicantInfo);

            Exam savedExam = new Exam();

            when(examService.save(any(Exam.class))).thenReturn(savedExam);
            when(applicantService.create(any(NewApplicantTaskDto.class))).thenReturn(new Applicant());

            String result = tokenService.generateApplicantToken(applicantTaskDto);

            assertNotNull(result);

            verify(examService).save(any(Exam.class));
        }

        @Test
        public void testGenerateApplicantToken_Negative() {

            NewApplicantDto applicantInfo = new NewApplicantDto("Ivan", "Ivanov", "ivanov@gmail.com", "Berlin", "0123456789");
            NewTestsParamDto newTestsParamDto = new NewTestsParamDto(1L, 2, 10);
            List<NewTestsParamDto> examTaskDtoList = Collections.singletonList(newTestsParamDto);

            NewApplicantTaskDto applicantTaskDto = new NewApplicantTaskDto("Exam1", 30, examTaskDtoList, applicantInfo);

            when(examService.save(any(Exam.class))).thenThrow(new DataIntegrityViolationException("Exam with ID 1 already exists."));

            assertThrows(DataIntegrityViolationException.class, () -> tokenService.generateApplicantToken(applicantTaskDto));

            verify(examService).save(any(Exam.class));
        }
    }

    @Nested
    @DisplayName("Save the token code")
    class saveTokenCodeTests {

        @Test
        public void saveTokenCode_PositiveTest() {

            String codeValue = "token_code";
            Applicant applicant = new Applicant();
            ExamTask examTask = new ExamTask();

            tokenService.saveTokenCode(codeValue, applicant, examTask);

            verify(tokenRepository).save(any(Token.class));
        }

        @Test
        void testSaveTokenCodeWithNullApplicant() {

            String codeValue = "code";
            Applicant applicant = null;
            ExamTask examTask = new ExamTask(); // Create a dummy ExamTask

            assertThrows(IllegalArgumentException.class,
                    () -> tokenService.saveTokenCode(codeValue, applicant, examTask));
        }
    }

    @Nested
    @DisplayName("Checking for token code exists")
    class tokenCheckTests {

        @Test
        public void testTokenCheck_WhenTokenIsValid_ReturnsApplicantDto() {

            String tokenCode = "validTokenCode";
            Token token = new Token();
            token.setCode(tokenCode);
            Applicant applicant = new Applicant();
            applicant.setFirstName("Ivan");
            applicant.setLastName("Ivanov");

            when(tokenRepository.findByCodeAndExpiredDateTimeAfter(eq(tokenCode), any(LocalDateTime.class)))
                    .thenReturn(Optional.of(token));
            when(applicantRepository.findFirstByTokensCode(eq(tokenCode))).thenReturn(Optional.of(applicant));

            ApplicantDto result = tokenService.tokenCheck(tokenCode);

            assertNotNull(result);
            assertEquals("Ivan", result.getFirstName());
            assertEquals("Ivanov", result.getLastName());
            verify(applicantRepository, times(1)).save(applicant);
        }

        @Test
        public void testTokenCheck_WhenTokenIsExpired_ThrowsRestException() {

            String expiredTokenCode = "expiredTokenCode";

            when(tokenRepository.findByCodeAndExpiredDateTimeAfter(eq(expiredTokenCode), any(LocalDateTime.class)))
                    .thenReturn(Optional.empty());
            assertThrows(RestException.class, () -> tokenService.tokenCheck(expiredTokenCode));
        }

        @Test
        public void testTokenCheck_WhenTokenNotFound_ThrowsRestException() {

            String nonExistentTokenCode = "nonExistentTokenCode";

            when(tokenRepository.findByCodeAndExpiredDateTimeAfter(eq(nonExistentTokenCode), any(LocalDateTime.class)))
                    .thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> tokenService.tokenCheck(nonExistentTokenCode));
        }
    }

    @Nested
    @DisplayName("Checking for token code exists")
    class getByCodeOrThrowTests {

        @Test
        void testGetByCodeOrThrow_WhenTokenExists() {

            Token token = new Token();
            String tokenCode = "abc123";

            when(tokenRepository.findByCode(tokenCode)).thenReturn(Optional.of(token));

            assertDoesNotThrow(() -> tokenService.getByCodeOrThrow(tokenCode));
            verify(tokenRepository, times(1)).findByCode(tokenCode);
        }

        @Test
        void testGetByCodeOrThrow_WhenTokenDoesNotExist() {

            when(tokenRepository.findByCode(anyString())).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> tokenService.getByCodeOrThrow("nonExistentCode"));

            verify(tokenRepository, times(1)).findByCode("nonExistentCode");
        }
    }
}
