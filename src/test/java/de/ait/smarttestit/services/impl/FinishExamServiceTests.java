package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam.NewFinishExamDto;
import de.ait.smarttestit.models.Answer;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.ExamStatus;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.repositories.ExamRepository;
import de.ait.smarttestit.services.AnswerService;
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
import java.util.List;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FinishExam:")
class FinishExamServiceTests {

    @Mock
    private ExamService examService;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private FinishExamServiceImpl finishExamService;

    @InjectMocks
    private ExamServiceImpl examsServices;

    private NewFinishExamDto newFinishExamDto;

    private Exam exam;

    @Nested
    @DisplayName("Update Exam At Finish")
    class updateExamAtFinish {

        @BeforeEach
        void setUp() {

            newFinishExamDto = new NewFinishExamDto(List.of(1, 2, 3), 1L);

            lenient().when(answerService.getByIdOrThrow(1L)).thenReturn(new Answer("Answer_1", true, new Question()));
            lenient().when(answerService.getByIdOrThrow(2L)).thenReturn(new Answer("Answer_2", true, new Question()));
            lenient().when(answerService.getByIdOrThrow(3L)).thenReturn(new Answer("Answer_3", true, new Question()));

            exam = new Exam();
            exam.setId(1L);
            exam.setExamStatus(ExamStatus.UNDERWAY);
        }

        @Test
        void updateExamAtFinishShouldUpdateExamCorrectly() {

            when(examService.getExamOrThrow(newFinishExamDto.examId())).thenReturn(exam);

            finishExamService.updateExamAtFinish(newFinishExamDto);

            int score = examsServices.getExamScore(newFinishExamDto);
            exam.setExamScore(score);

            assertEquals(ExamStatus.COMPLETED, exam.getExamStatus());
            assertEquals(100, exam.getExamScore());
            assertNotNull(exam.getExamEndTime());
            verify(examRepository).save(exam);
        }

        @Test
        void updateExamAtFinishShouldThrowExceptionWhenExamNotFound() {

            when(examService.getExamOrThrow(newFinishExamDto.examId())).thenThrow(new EntityNotFoundException("Exam not found"));

            EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> finishExamService.updateExamAtFinish(newFinishExamDto), "EntityNotFoundException should be thrown");
            assertEquals("Exam not found", thrown.getMessage());
        }
    }

    @Nested
    @DisplayName("Finish Exam")
    class finishExam {

        @BeforeEach
        void setUp() {
            newFinishExamDto = new NewFinishExamDto(List.of(1, 2, 3), 1L);
            exam = new Exam();
            exam.setId(1L);
            exam.setExamStatus(ExamStatus.UNDERWAY);
        }

        @Test
        void finishExamShouldReturnSuccessMessage() {

            when(examService.getExamOrThrow(newFinishExamDto.examId())).thenReturn(exam);
            when(examService.getExamScore(newFinishExamDto)).thenReturn(80);
            when(examRepository.save(any(Exam.class))).thenReturn(exam);

            String result = finishExamService.finishExam(newFinishExamDto);

            assertEquals("Thank you, the exam has been successfully completed.", result);
            verify(examService).getExamOrThrow(newFinishExamDto.examId());
            verify(examService).getExamScore(newFinishExamDto);
            verify(examRepository).save(exam);
        }

        @Test
        void finishExamShouldThrowExceptionWhenExamNotFound() {

            when(examService.getExamOrThrow(newFinishExamDto.examId())).thenThrow(new EntityNotFoundException("Exam not found"));

            EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                    () -> finishExamService.finishExam(newFinishExamDto),
                    "EntityNotFoundException should be thrown");

            assertEquals("Exam not found", thrown.getMessage());
            verify(examService).getExamOrThrow(newFinishExamDto.examId());
            verify(examService, never()).getExamScore(newFinishExamDto);
            verify(examRepository, never()).save(any(Exam.class));
        }
    }
}
