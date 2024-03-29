package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.dto.exam_task.NewTestsParamDto;
import de.ait.smarttestit.dto.question.NewQuestionDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.dto.question.UpdateQuestionDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.QuestionRepository;
import de.ait.smarttestit.services.TestTypeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
@Transactional
public class QuestionServiceTest {

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TestTypeService testTypeService;

    @Nested
    @DisplayName("add question")
    class addQuestion {

        @Test
        void addQuestion_PositiveTest() {

            Long testTypeId = 1L;
            NewQuestionDto newQuestion = new NewQuestionDto("What is your question?", 1);
            TestType testType = new TestType();
            when(questionRepository.existsByQuestionTextAndTestTypeId(
                    newQuestion.questionText(), testTypeId)).thenReturn(false);

            Question question = new Question();
            question.setQuestionText(newQuestion.questionText());
            question.setLevel(newQuestion.level());
            question.setTestType(testType);
            when(questionRepository.save(any(Question.class))).thenReturn(question);

            QuestionDto result = questionService.addQuestion(testTypeId, newQuestion);

            assertNotNull(result);
            assertEquals(newQuestion.questionText(), result.questionText());
            assertEquals(newQuestion.level(), result.level());
        }

        @Test
        void addQuestion_NegativeTest() {

            Long testTypeId = 1L;
            NewQuestionDto newQuestion = new NewQuestionDto("What is your question?", 1);
            TestType testType = new TestType();
            when(questionRepository.existsByQuestionTextAndTestTypeId(
                    newQuestion.questionText(), testTypeId)).thenReturn(true);

            assertThrows(RestException.class, () -> questionService.addQuestion(testTypeId, newQuestion));
        }
    }

    @Nested
    @DisplayName("get all questions")
    class getAllQuestions {

        @Test
        void getAllQuestionsTest() {

            TestType testType1 = new TestType();
            TestType testType2 = new TestType();

            List<Question> questionList = Arrays.asList(
                    new Question("What is ... question 1?", 1, testType1),
                    new Question("What is ... question 2?", 2, testType2)
            );

            when(questionRepository.findAll()).thenReturn(questionList);

            List<QuestionDto> result = questionService.getAll();

            assertEquals(2, result.size());
            assertEquals("What is ... question 1?", result.get(0).questionText());
            assertEquals(1, result.get(0).level());
            assertEquals("What is ... question 2?", result.get(1).questionText());
            assertEquals(2, result.get(1).level());
        }
    }

    @Nested
    @DisplayName("test get by id")
    class getById {
        @Test
        void testGetByIdOrThrow_returnsQuestion_whenIdExists() {

            Question question = new Question();
            question.setId(1L);
            when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));

            Question result = questionService.getByIdOrThrow(1L);

            verify(questionRepository).findById(1L);
            assertEquals(question, result);
        }

        @Test
        void testGetByIdOrThrow_throwsRestException_whenIdDoesNotExist() {

            long questionId = 1L;
            when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

            RestException exception = assertThrows(RestException.class,
                    () -> questionService.getByIdOrThrow(questionId));

            verify(questionRepository).findById(eq(questionId));
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("Question with id <" + questionId + "> not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("update question")
    class updateQuestion {

        @Test
        void updateQuestion_PositiveTest() {

            Long testTypeId = 3L;
            Long questionId = 2L;

            UpdateQuestionDto updateQuestion = new UpdateQuestionDto("Updated Question Text", 2, 3L);

            TestType existendTestType = new TestType(testTypeId, "TestType", null, null);
            Question existingQuestion = new Question("OldQuestion", 1, existendTestType);

            when(testTypeService.getByIdOrThrow(testTypeId)).thenReturn(existendTestType);
            when(questionRepository.findById(questionId)).thenReturn(Optional.of(existingQuestion));
            when(questionRepository.save(any(Question.class))).thenReturn(existingQuestion);

            QuestionDto result = questionService.updateQuestion(testTypeId, questionId, updateQuestion);

            assertNotNull(result);
            assertEquals(updateQuestion.questionText(), result.questionText());
            assertEquals(updateQuestion.level(), result.level());
            assertEquals(updateQuestion.testTypeId(), result.testTypeId());

            verify(questionRepository).save(
                    argThat(saved -> saved.getTestType().getId().equals(updateQuestion.testTypeId())
                            && saved.getLevel().equals(updateQuestion.level())
                            && saved.getQuestionText().equals(updateQuestion.questionText())
                    )
            );
            verify(testTypeService).getByIdOrThrow(testTypeId);
        }

        @Test
        void updateQuestion_NegativeTest() {

            Long testTypeId = 1L;
            Long questionId = 2L;
            UpdateQuestionDto updateQuestionDto = new UpdateQuestionDto("Updated Question Text", 2, 3L);

            when(testTypeService.getByIdOrThrow(any()))
                    .thenThrow(new IllegalArgumentException("Invalid test type"));

            assertThrows(IllegalArgumentException.class,
                    () -> questionService.updateQuestion(testTypeId, questionId, updateQuestionDto));

            verify(testTypeService, times(1)).getByIdOrThrow(any());
            verifyNoMoreInteractions(testTypeService);
            verifyNoInteractions(questionRepository);
        }
    }

    @Nested
    @DisplayName("delete question")
    class deleteQuestion {

        @Test
        void testDeleteQuestion_PositiveTest() {

            Long questionId = 1L;
            Question existingQuestion = new Question("OldQuestion", 1, new TestType("TestType"));

            when(questionRepository.findById(questionId)).thenReturn(Optional.of(existingQuestion));

            QuestionDto result = questionService.deleteQuestion(questionId);

            assertNotNull(result);
            assertEquals(existingQuestion.getQuestionText(), result.questionText());

            verify(questionRepository, times(1)).delete(existingQuestion);
        }

        @Test
        void testDeleteQuestion_WhenQuestionNotFound() {

            Long questionId = 1L;

            when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> questionService.deleteQuestion(questionId));

            verify(questionRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Create test type for exam")
    class createTestTypeForExam {

        @Test
        void createTestTypeForExam_PositiveTest() {

            Long testTypeId = 1L;
            int questionLevel = 2;
            int questionCount = 2;
            TestType testType = new TestType(String.valueOf(testTypeId));

            List<Question> questions = Arrays.asList(
                    new Question(1L, "Question 1", questionLevel, testType, null),
                    new Question(2L, "Question 2", questionLevel, testType, null)
            );
            when(questionService.getListQuestionsForTestType(testTypeId, questionLevel, questionCount))
                    .thenReturn(questions);

            TestType result = questionService.createTestTypeForExam(testTypeId, questionLevel, questionCount);

            Collections.sort(questions, Comparator.comparing(Question::getId));
            Collections.sort(result.getQuestions(), Comparator.comparing(Question::getId));

            assertEquals(questions, result.getQuestions());
        }

        @Test
        void createTestTypeForExam_NegativeTest() {

            Long testTypeId = 1L;
            int questionLevel = 2;
            int questionCount = 3;

            when(questionService.getListQuestionsForTestType(testTypeId, questionLevel, questionCount))
                    .thenReturn(null);
            assertThrows(NullPointerException.class, () -> questionService.createTestTypeForExam(testTypeId, questionLevel, questionCount));
        }
    }

    @Nested
    @DisplayName("Create list of test types for exam")
    class createListTestType {

        @Test
        void testCreateTestTypesForExam_PositiveTest() {
            NewApplicantTaskDto applicantTaskDto = mock(NewApplicantTaskDto.class);

            NewTestsParamDto testParam1 = new NewTestsParamDto(1L, 2, 5);
            NewTestsParamDto testParam2 = new NewTestsParamDto(2L, 3, 7);

            when(applicantTaskDto.examTaskDtoList()).thenReturn(Arrays.asList(testParam1, testParam2));
            List<TestType> result = questionService.createTestTypesForExam(applicantTaskDto);

            assertFalse(result.isEmpty());
        }

        @Test
        void createTestTypesForExam_NegativeTest() {

            NewApplicantTaskDto applicantTaskDto = new NewApplicantTaskDto(null, 0, null, null);
            try {
                Objects.requireNonNull(applicantTaskDto.examTitle(), "Exam title must not be null");
                Objects.requireNonNull(applicantTaskDto.examTaskDtoList(), "Exam task DTO list must not be null");
                Objects.requireNonNull(applicantTaskDto.applicantInfo(), "Applicant info must not be null");

                questionService.createTestTypesForExam(applicantTaskDto);
                fail("Expected NullPointerException was not thrown");
            } catch (NullPointerException ignored) {
            } catch (IllegalArgumentException e) {
                fail("Unexpected IllegalArgumentException was thrown");
            }
        }
    }

    @Nested
    @DisplayName("Getting a list of question on a specific test type")
    class getListQuestionsForTestType {

        @Test
        void getListQuestionsForTestType_PositiveTest() {
            openMocks(this);

            long testTypeId = 1L;
            int questionLevel = 2;
            int questionCount = 5;

            List<Question> mockQuestions = new ArrayList<>();
            mockQuestions.add(new Question(1L, "Question 1", 1));
            mockQuestions.add(new Question(2L, "Question 2", 2));
            mockQuestions.add(new Question(3L, "Question 3", 2));
            mockQuestions.add(new Question(4L, "Question 4", 3));
            mockQuestions.add(new Question(5L, "Question 5", 2));

            when(questionRepository.findAllByTestTypeIdAndLevel(testTypeId, questionLevel))
                    .thenReturn(mockQuestions);

            List<Question> result = questionService
                    .getListQuestionsForTestType(testTypeId, questionLevel, questionCount);

            assertFalse(result.isEmpty());
            assertEquals(questionCount, result.size());
        }

        @Test
        void testGetListQuestionsForTestTypeWhenNoQuestionsFound () {

            Long testTypeId = 1L;
            int questionLevel = 2;
            int questionCount = 5;

            List<Question> result = questionService
                    .getListQuestionsForTestType(testTypeId, questionLevel, questionCount);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
