package de.ait.smarttestit.dto.question;

import de.ait.smarttestit.models.Answer;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionDtoTest {

    @Test
    @DisplayName("Test conversion from null Question object")
    void shouldReturnNullWhenFromIsCalledWithNull() {
        Question question = null;
        assertNull(QuestionDto.from(question));
    }

    @Test
    @DisplayName("Test conversion from Question object with all fields filled")
    void shouldReturnQuestionDtoWithAllFieldsFilledWhenFromIsCalledWithQuestion() {
        // given
        TestType testType = new TestType();
        testType.setId(1L);

        Answer answer = new Answer();
        answer.setId(2L);

        Question question = new Question(3L, "Question text", 4, testType, List.of(answer));

        // when
        QuestionDto questionDto = QuestionDto.from(question);

        // then
        assertEquals(question.getId(), questionDto.id());
        assertEquals(question.getQuestionText(), questionDto.questionText());
        assertEquals(question.getLevel(), questionDto.level());
        assertEquals(question.getTestType().getId(), questionDto.testTypeId());
       // assertEquals(question.getAnswers(), questionDto.answers());
    }

  /*  @Test
    @DisplayName("Test conversion from Question object with null TestType and Answers fields")
    void shouldReturnQuestionDtoWithNullTestTypeAndAnswersWhenFromIsCalledWithQuestion() {
        // given
        Question question = new Question(1L, "Question text", 2, null, null);

        // when
        QuestionDto questionDto = QuestionDto.from(question);

        // then
        assertEquals(question.getId(), questionDto.id());
        assertEquals(question.getQuestionText(), questionDto.questionText());
        assertEquals(question.getLevel(), questionDto.level());
        assertNull(questionDto.testTypeId());
        assertNull(questionDto.answers());
    }*/
}
