package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.answer.AnswerJsonDto;
import de.ait.smarttestit.dto.exam_task.ExamTaskWithTestTypeDto;
import de.ait.smarttestit.dto.question.QuestionWithAnswersDto;
import de.ait.smarttestit.dto.test_type.TestTypeWithQuestionDto;
import de.ait.smarttestit.models.*;
import de.ait.smarttestit.repositories.ExamRepository;
import de.ait.smarttestit.services.ExamService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExamStart:")
public class ExamStartServiceTests {

    @Mock
    private ExamService examService;

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private StartExamServiceImpl startExamService;

    private Exam existingExam;

    private Exam exam;

    private final Long examId = 1L;

    @BeforeEach
    void setUp() {

        existingExam = new Exam();
        existingExam.setId(examId);
        existingExam.setExamStatus(ExamStatus.PLANNED);

        ExamTask examTask = new ExamTask();
        examTask.setId(1L);
        examTask.setExamTaskTitle("Java Basics");

        exam = new Exam();
        exam.setId(examId);
        exam.setExamStatus(ExamStatus.UNDERWAY);
        exam.setExamStartTime(LocalDateTime.now());
        exam.setExamTask(examTask);

        examTask.setExam(exam);

        TestType testType = new TestType();
        testType.setId(1L);
        testType.setName("Java Basics");
        testType.setExamTask(examTask);

        Question question = new Question();
        question.setId(1L);
        question.setQuestionText("What is an interface in Java?");
        question.setLevel(5);
        question.setTestType(testType);

        Answer answer = new Answer();
        answer.setId(1L);
        answer.setAnswerText("An interface is a contract.");
        answer.setCorrect(true);
        answer.setQuestion(question);

        testType.getQuestions().add(question);
        question.getAnswers().add(answer);
        examTask.getTestTypes().add(testType);
    }

    @Nested
    @DisplayName("complementsExamAtStartup")
    class complementsExamAtStartup {

        @Test
        void complementsExamAtStartupShouldUpdateExamDetails() {

            when(examService.getExamOrThrow(examId)).thenReturn(existingExam);
            when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Exam updatedExam = startExamService.complementsExamAtStartup(examId);

            assertNotNull(updatedExam.getExamStartTime(), "Exam start time should be set");
            assertEquals(ExamStatus.UNDERWAY, updatedExam.getExamStatus(), "Exam status should be updated to UNDERWAY");
            verify(examService).getExamOrThrow(examId);
            verify(examRepository).save(updatedExam);
        }

        @Test
        void complementsExamAtStartupShouldThrowWhenExamNotFound() {

            when(examService.getExamOrThrow(examId)).thenThrow(new EntityNotFoundException("Exam not found"));

            EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> startExamService.complementsExamAtStartup(examId), "EntityNotFoundException should be thrown");
            assertEquals("Exam not found", thrown.getMessage(), "Exception message should match expected");
            verify(examService).getExamOrThrow(examId);
            verifyNoInteractions(examRepository);
        }
    }

    @Nested
    @DisplayName("getExamTaskForExam")
    class getExamTaskForExam {

        @Test
        void getExamTaskForExamShouldReturnValidDtoWithCorrectStructure() {

            when(examService.getExamOrThrow(examId)).thenReturn(exam);
            when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ExamTaskWithTestTypeDto result = startExamService.getExamTaskForExam(examId);

            assertNotNull(result);
            assertEquals(examId, result.examId());
            assertNotNull(result.testTypes());
            assertEquals(1, result.testTypes().size());
            TestTypeWithQuestionDto testTypeDto = result.testTypes().get(0);
            assertEquals("Java Basics", testTypeDto.name());
            assertNotNull(testTypeDto.questions());
            assertEquals(1, testTypeDto.questions().size());
            QuestionWithAnswersDto questionDto = testTypeDto.questions().get(0);
            assertEquals("What is an interface in Java?", questionDto.questionText());
            assertNotNull(questionDto.answers());
            assertEquals(1, questionDto.answers().size());
            AnswerJsonDto answerDto = questionDto.answers().get(0);
            assertEquals("An interface is a contract.", answerDto.answerText());
        }

        @Test
        void complementsExamAtStartupShouldThrowWhenExamNotFound() {

            when(examService.getExamOrThrow(examId)).thenThrow(new EntityNotFoundException("Exam not found"));

            EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> startExamService.complementsExamAtStartup(examId), "EntityNotFoundException should be thrown");
            assertEquals("Exam not found", thrown.getMessage(), "Exception message should match expected");
            verify(examService).getExamOrThrow(examId);
            verifyNoInteractions(examRepository);
        }
    }
}
