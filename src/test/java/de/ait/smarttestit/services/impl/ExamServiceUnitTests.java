package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam.ExamDto;
import de.ait.smarttestit.dto.exam.NewExamDto;
import de.ait.smarttestit.dto.exam.UpdateExamDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.*;
import de.ait.smarttestit.repositories.ExamRepository;
import de.ait.smarttestit.services.ApplicantService;
import de.ait.smarttestit.services.ExamTasksService;
import de.ait.smarttestit.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Exam CRUD:")
public class ExamServiceUnitTests {
    private static final Long EXAM_Id = 1L;
    private static final int EXAM_SCORE = 70;
    private static final LocalDateTime DEFAULT_DATE_START = LocalDateTime.now();
    private static final LocalDateTime DEFAULT_DATE_EXPIRATION = LocalDateTime.now().plusHours(1);
    private static final int EXAM_DURATION = 60;
    private static final ExamStatus EXAM_STATUS = ExamStatus.COMPLETED;
    private static final String STATUS_OF_EXAM = "COMPLETED";
    private static final Long USER_ID = 1L;
    private static final Long APPLICANT_ID = 1L;
    private static final Long TEST_ID = 5L;
    private static final String TEST_TITLE = "General";
    private static final List<Exam> EXAMS = new ArrayList<>();
    private static final List<TestType> TEST_TYPES = new ArrayList<>();
    private static final Long USER_Id = 1L;
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String EMAIL = "simple@mail.com";
    private static final String PASSWORD = "password";
    private static final UserRole USER_ROLE = UserRole.USER;
    private static final int LEVEL_OF_USER = 5;
    private static final User USER = new User(USER_Id, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USER_ROLE, LEVEL_OF_USER);
    private static final Applicant APPLICANT = new Applicant(APPLICANT_ID, "John", "Doe",
            "john.doe@example.com", "123 Main St", "555-1234", EXAMS,null);
    private static final ExamTask EXAM_TASK = new ExamTask();
    private static final Exam EXAM = new Exam(EXAM_Id, EXAM_SCORE, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION,
            EXAM_DURATION, EXAM_STATUS, USER, APPLICANT, EXAM_TASK);
    private static final NewExamDto NEW_EXAM_DTO_USER_APPLICANT = new NewExamDto(EXAM_SCORE, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION,
            EXAM_DURATION, STATUS_OF_EXAM, USER_ID, APPLICANT_ID, TEST_ID);
    private static final NewExamDto NEW_EXAM_DTO_USER_NULL_APPLICANT_NULL = new NewExamDto(EXAM_SCORE, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION,
            EXAM_DURATION, STATUS_OF_EXAM, null, null, TEST_ID);
    private static final NewExamDto NEW_EXAM_DTO_USER_NULL = new NewExamDto(EXAM_SCORE, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION,
            EXAM_DURATION, STATUS_OF_EXAM, null, APPLICANT_ID, TEST_ID);
    private static final NewExamDto NEW_EXAM_DTO_APPLICANT_NULL = new NewExamDto(EXAM_SCORE, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION,
            EXAM_DURATION, STATUS_OF_EXAM, USER_ID, null, TEST_ID);

    @Mock
    private ExamRepository examRepository;
    @Mock
    private UserService userService;
    @Mock
    private ApplicantService applicantService;
    @Mock
    private ExamTasksService examTasksService;
    @InjectMocks
    private ExamServiceImpl examsServices;

    @Nested
    @DisplayName("get exam or trow")
    class GetExamOrThrow {

        @Test
        void getExamOrThrowPositive() {

            when(examRepository.findById(EXAM_Id)).thenReturn(Optional.of(EXAM));

            Exam foundExam = examsServices.getExamOrThrow(EXAM_Id);

            assertNotNull(foundExam, "Exam should not be null");
            assertEquals(EXAM_Id, foundExam.getId(), "Exam ID should match the requested ID");
        }

        @Test
        void getExamOrThrowNegative() {
            when(examRepository.findById(EXAM_Id)).thenReturn(Optional.empty());

            RestException thrown = assertThrows(RestException.class, () -> examsServices.getExamOrThrow(EXAM_Id),
                    "Expected getExamOrThrow to throw, but it didn't");

            assertTrue(thrown.getMessage().contains("Exam with id <" + EXAM_Id + "> not found"), "Exception message should contain the correct exam ID");
        }
    }

    @Nested
    @DisplayName("find exam status by name")
    class ExamStatusTest {

        @Test
        void testFindByNamePositive() {

            assertAll(
                    () -> assertEquals(Optional.of(ExamStatus.PLANNED), ExamStatus.findByName("PLANNED")),
                    () -> assertEquals(Optional.of(ExamStatus.UNDERWAY), ExamStatus.findByName("underway")),
                    () -> assertEquals(Optional.of(ExamStatus.COMPLETED), ExamStatus.findByName("Completed"))
            );
        }

        @Test
        void testFindByNameNegative() {

            assertEquals(Optional.empty(), ExamStatus.findByName("INCORRECT_VALUE_EXAM_STATUS"));
        }

        @Test
        void testFindByName_NullArgument() {

            assertEquals(Optional.empty(), ExamStatus.findByName(null));
        }
    }

    @Nested
    @DisplayName("add exam")
    class AddExam {

        @Test
        void testAddExamUserNotNullPositive() {

            when(userService.getUserOrThrow(NEW_EXAM_DTO_APPLICANT_NULL.userId())).thenReturn(USER);
            when(examTasksService.getByIdOrThrow(NEW_EXAM_DTO_APPLICANT_NULL.examTaskId())).thenReturn(EXAM_TASK);
            when(examRepository.save(any(Exam.class))).thenReturn(EXAM);

            ExamDto examDto = examsServices.addExam(NEW_EXAM_DTO_APPLICANT_NULL);

            assertEquals(EXAM_SCORE, examDto.examScore());
            assertEquals(DEFAULT_DATE_START, LocalDateTime.parse(examDto.examStartTime()));
            assertEquals(DEFAULT_DATE_EXPIRATION, LocalDateTime.parse(examDto.examEndTime()));
            assertEquals(EXAM_DURATION, examDto.examDuration());
            assertEquals(STATUS_OF_EXAM, examDto.examStatus());
            assertEquals(USER_ID, examDto.userId());
            assertEquals(APPLICANT_ID, examDto.applicantId());
           // assertEquals(TEST_ID, examDto.examParams());

            verify(examRepository).save(any(Exam.class));
        }

        @Test
        void testAddExamApplicantNotNullPositive() {

            when(applicantService.getApplicantOrThrow(NEW_EXAM_DTO_USER_NULL.applicantId())).thenReturn(APPLICANT);
            when(examTasksService.getByIdOrThrow(NEW_EXAM_DTO_USER_NULL.examTaskId())).thenReturn(EXAM_TASK);
            when(examRepository.save(any(Exam.class))).thenReturn(EXAM);

            ExamDto examDto = examsServices.addExam(NEW_EXAM_DTO_USER_NULL);

            assertEquals(EXAM_SCORE, examDto.examScore());
            assertEquals(DEFAULT_DATE_START, LocalDateTime.parse(examDto.examStartTime()));
            assertEquals(DEFAULT_DATE_EXPIRATION, LocalDateTime.parse(examDto.examEndTime()));
            assertEquals(EXAM_DURATION, examDto.examDuration());
            assertEquals(STATUS_OF_EXAM, examDto.examStatus());
            assertEquals(USER_ID, examDto.userId());
            assertEquals(APPLICANT_ID, examDto.applicantId());
           // assertEquals(TEST_ID, examDto.examParams());

            verify(examRepository).save(any(Exam.class));
        }

        @Test
        void testAddExamNegative_IncorrectValueExamStatus() {

            NewExamDto newExam = new NewExamDto(EXAM_SCORE, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION, EXAM_DURATION,
                    "INCORRECT_VALUE_EXAM_STATUS", APPLICANT_ID, USER_ID, TEST_ID);

            assertThrows(RestException.class, () -> examsServices.addExam(newExam));
        }

        @Test
        void testAddExamNegative_WhenUserAndApplicantNotFound() {

            RestException thrown = assertThrows(RestException.class, () -> examsServices.addExam(NEW_EXAM_DTO_USER_NULL_APPLICANT_NULL));

            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("Neither userId nor applicantId provided", thrown.getMessage());
        }

        @Test
        void testAddExamNegative_WhenUserNotNullAndApplicantNotNull() {

            RestException thrown = assertThrows(RestException.class, () -> examsServices.addExam(NEW_EXAM_DTO_USER_APPLICANT));

            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("Both user and applicant are defined", thrown.getMessage());
        }

    }

    @Nested
    @DisplayName("get list exams")
    class GetListExams {

        @Test
        void testGetListExamsPositive() {

            List<Exam> exams = new ArrayList<>();
            exams.add(EXAM);
            List<ExamDto> actual = exams.stream()
                    .map(ExamDto::from)
                    .collect(Collectors.toList());
            given(examRepository.findAll()).willReturn(exams);
            List<ExamDto> expected = examsServices.getListExams();
            assertEquals(expected, actual);
            verify(examRepository).findAll();
        }

        @Test
        void testGetListExamsNegative() {

            List<Exam> exams = new ArrayList<>();
            given(examRepository.findAll()).willReturn(exams);
            List<ExamDto> expected = examsServices.getListExams();

            RestException exception = Assertions.assertThrows(RestException.class, () -> {
                if (expected.isEmpty()) {
                    throw new RestException(HttpStatus.NOT_FOUND,
                            "The Exam list is empty");
                }
            });
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertThat(exception.getMessage().trim(),
                    containsString("The Exam list is empty"));
        }
    }

    @Nested
    @DisplayName("get exam")
    class GetExam {

        @Test
        void testGetExamPositive() {

            when(examRepository.findById(EXAM_Id)).thenReturn(Optional.of(EXAM));

            ExamDto actual = ExamDto.from(EXAM);
            ExamDto expected = examsServices.getExam(EXAM_Id);

            assertEquals(expected, actual);
        }

        @Test
        void testGetExamNegative() {

            Long examId = 5L;

            RestException exception = assertThrows(RestException.class,
                    () -> examsServices.getExam(examId));

            assertThat(exception.getMessage().trim(),
                    containsString("Exam with id <" + examId + "> not found"));
        }
    }

    @Nested
    @DisplayName("update exam")
    class updateExam {

        @Test
        void testUpdateExamPositive() {

            Exam exam = new Exam(2L, EXAM_SCORE, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION,
                    EXAM_DURATION, EXAM_STATUS, USER, APPLICANT, EXAM_TASK);

            UpdateExamDto updateExam = new UpdateExamDto(EXAM_SCORE + 10, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION,
                    EXAM_DURATION, STATUS_OF_EXAM, USER_ID, APPLICANT_ID, TEST_ID);

            when(examRepository.findById(2L)).thenReturn(Optional.of(exam));
            when(userService.getUserOrThrow(updateExam.userId())).thenReturn(USER);
            when(examTasksService.getByIdOrThrow(updateExam.examTaskId())).thenReturn(EXAM_TASK);
            when(examRepository.save(any(Exam.class))).thenReturn(exam);

            ExamDto examDto = examsServices.updateExam(2L, updateExam);

            assertEquals(2L, examDto.id());
            assertEquals(80, examDto.examScore());
            assertEquals(DEFAULT_DATE_START, LocalDateTime.parse(examDto.examStartTime()));
            assertEquals(DEFAULT_DATE_EXPIRATION, LocalDateTime.parse(examDto.examEndTime()));
            assertEquals(EXAM_DURATION, examDto.examDuration());
            assertEquals(STATUS_OF_EXAM, examDto.examStatus());
            assertEquals(USER_ID, examDto.userId());
            //assertEquals(TEST_ID, examDto.examParams());

            verify(examRepository).save(any(Exam.class));
        }

        @Test
        void testUpdateExamNegative() {

            Long examId = 2L;

            UpdateExamDto updateExam = new UpdateExamDto(EXAM_SCORE + 10, DEFAULT_DATE_START, DEFAULT_DATE_EXPIRATION,
                    EXAM_DURATION, STATUS_OF_EXAM, APPLICANT_ID, USER_ID, TEST_ID);

            RestException exception = assertThrows(RestException.class,
                    () -> examsServices.updateExam(examId, updateExam));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("Exam with id <" + examId + "> not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("delete exam")
    class deleteExam {

        @Test
        void testDeleteExamPositive() {

            when(examRepository.findById(EXAM_Id)).thenReturn(Optional.of(EXAM));

            ExamDto deleteExam = examsServices.deleteExam(EXAM_Id);

            assertEquals(EXAM_Id, deleteExam.id());
            assertEquals(EXAM_SCORE, deleteExam.examScore());
            assertEquals(DEFAULT_DATE_START, parse(deleteExam.examStartTime()));
            assertEquals(DEFAULT_DATE_EXPIRATION, parse(deleteExam.examEndTime()));
            assertEquals(EXAM_DURATION, deleteExam.examDuration());
            assertEquals(STATUS_OF_EXAM, deleteExam.examStatus());
            assertEquals(USER_ID, deleteExam.userId());
           // assertEquals(TEST_ID, deleteExam.examParams());

            verify(examRepository, times(1)).delete(EXAM);
        }

        @Test
        void testDeleteExamNegative() {

            Long examId = 5L;

            RestException exception = assertThrows(RestException.class,
                    () -> examsServices.deleteExam(examId));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("Exam with id <" + examId + "> not found", exception.getMessage());
        }
    }
}

