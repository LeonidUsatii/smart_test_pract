package de.ait.smarttestit;

import de.ait.smarttestit.dto.answer.AnswerDto;
import de.ait.smarttestit.dto.answer.NewAnswerDto;
import de.ait.smarttestit.dto.answer.UpdateAnswerDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Answer;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.repositories.AnswersRepository;
import de.ait.smarttestit.services.QuestionService;
import de.ait.smarttestit.services.impl.AnswerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class AnswersUnitTests {

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Mock
    private AnswersRepository answersRepository;

    @Mock
    private QuestionService questionService;

    @Nested
    @DisplayName("add answer")
    class addAnswer {

        @Test
        void addAnswer_PositiveTest() {
            Long questionId = 1L;
            NewAnswerDto newAnswer = new NewAnswerDto("Answer1", true);
            Question question = new Question();
            question.setId(questionId);

            when(questionService.getByIdOrThrow(questionId)).thenReturn(question);
            when(answersRepository.existsByAnswerTextAndQuestionId(newAnswer.answerText(), questionId)).thenReturn(false);

            Answer savedAnswer = new Answer();
            savedAnswer.setAnswerText(newAnswer.answerText());
            savedAnswer.setCorrect(newAnswer.isCorrect());
            savedAnswer.setQuestion(question);

            when(answersRepository.save(any(Answer.class))).thenReturn(savedAnswer);

            AnswerDto result = answerService.addAnswer(questionId, newAnswer);

            assertNotNull(result);
            assertEquals(newAnswer.answerText(), result.answerText());
            assertEquals(newAnswer.isCorrect(), result.isCorrect());
        }

        @Test
        void addAnswer_NegativeTest() {

            Long questionId = 1L;
            NewAnswerDto newAnswer = new NewAnswerDto("Answer1", true);
            Question question = new Question();
            when(questionService.getByIdOrThrow(questionId)).thenReturn(question);
            when(answersRepository.existsByAnswerTextAndQuestionId(
                    newAnswer.answerText(), questionId)).thenReturn(true);

            assertThrows(RestException.class, () -> answerService.addAnswer(questionId, newAnswer));
        }
    }

    @Nested
    @DisplayName("get all answers")
    class getAllAnswers {

        @Test
        void getAllAnswersTest() {

            Question question1 = new Question();

            List<Answer> answerList = Arrays.asList(
                    new Answer("Answer1", true,question1),
                    new Answer("Answer2", false, question1)
            );

            when(answersRepository.findAll()).thenReturn(answerList);

            List<AnswerDto> result = answerService.getAll();

            assertEquals(2, result.size());
            assertEquals("Answer1", result.get(0).answerText());
            assertTrue(true, String.valueOf(result.get(0).isCorrect()));
            assertEquals("Answer2", result.get(1).answerText());
            assertFalse(false, String.valueOf(result.get(1).isCorrect()));
        }
    }

    @Nested
    @DisplayName("answer get by id")

    class getById {
        @Test
        void testGetByIdOrThrow_returnsAnswer_whenIdExists() {
            Answer answer = new Answer();
            answer.setId(1L);
            when(answersRepository.findById(anyLong())).thenReturn(Optional.of(answer));

            Answer result = answerService.getByIdOrThrow(1L);

            verify(answersRepository).findById(1L);
            assertEquals(answer, result);
        }

        @Test
        void testGetByIdOrThrow_throwsRestException_whenIdDoesNotExist() {
            long answerId = 1L;
            when(answersRepository.findById(answerId)).thenReturn(Optional.empty());

            RestException exception = assertThrows(RestException.class,
                    () -> answerService.getByIdOrThrow(answerId));

            verify(answersRepository).findById(eq(answerId));
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("Answer with id <" + answerId + "> not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("update answer")
    class updateAnswer {

        @Test
        void updateAnswer_PositiveTest() {
            Long answerId = 2L;
            Long questionId = 1L;

            Question question = new Question();
            question.setId(questionId);

            UpdateAnswerDto updateAnswer = new UpdateAnswerDto("Updated Answer Text", true);
            Answer existingAnswer = new Answer("OldAnswer", false, question);
            existingAnswer.setId(answerId);

            when(answersRepository.findById(answerId)).thenReturn(Optional.of(existingAnswer));
            when(answersRepository.save(any(Answer.class))).thenReturn(existingAnswer);
            AnswerDto result = answerService.updateAnswer(questionId, answerId, updateAnswer);

            assertNotNull(result);
            assertEquals(updateAnswer.answerText(), result.answerText());
            assertEquals(updateAnswer.isCorrect(), result.isCorrect());

            verify(answersRepository).save(any(Answer.class));
        }

        @Test
        public void updateAnswer_NegativeTest() {

            Long questionId = 1L;
            Long answerId = 2L;
            UpdateAnswerDto updateAnswerDto = new UpdateAnswerDto("Updated Answer Text", true);

            when(questionService.getByIdOrThrow(any()))
                    .thenThrow(new IllegalArgumentException("Invalid question"));

            assertThrows(IllegalArgumentException.class, () -> answerService.updateAnswer( questionId, answerId, updateAnswerDto));

            verify(questionService, Mockito.times(1)).getByIdOrThrow(any());
            verifyNoMoreInteractions(questionService);
            verifyNoInteractions(answersRepository);
        }
    }

    @Nested
    @DisplayName("delete answer")
    class deleteAnswer {

        @Test
        void deleteAnswer_PositiveTest() {

            Long answerId = 1L;
            Answer existingAnswer = new Answer("OldAnswer", true,new Question());

            when(answersRepository.findById(answerId)).thenReturn(Optional.of(existingAnswer));

            AnswerDto result = answerService.deleteAnswer(answerId);

            assertNotNull(result);
            assertEquals(existingAnswer.getAnswerText(), result.answerText());

            verify(answersRepository, Mockito.times(1)).delete(existingAnswer);
        }

        @Test
        void testDeleteAnswer_WhenAnswerNotFound() {

            Long answerId = 1L;

            when(answersRepository.findById(answerId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> answerService.deleteAnswer(answerId));

            verify(answersRepository, never()).delete(any());
        }
    }
}