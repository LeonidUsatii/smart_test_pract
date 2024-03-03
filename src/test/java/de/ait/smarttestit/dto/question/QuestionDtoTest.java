package de.ait.smarttestit.dto.question;

import de.ait.smarttestit.models.Answer;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class QuestionDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Test conversion from null Question object")
    void shouldReturnNullWhenFromIsCalledWithNull() {
        Question question = null;
        assertNull(QuestionDto.from(question));
    }

    @Test
    @DisplayName("Test conversion from Question object with all fields filled")
    void shouldReturnQuestionDtoWithAllFieldsFilledWhenFromIsCalledWithQuestion() {

        TestType testType = new TestType();
        testType.setId(1L);

        Answer answer = new Answer();
        answer.setId(2L);

        Question question = new Question(3L, "Question text", 4, testType, List.of(answer));

        QuestionDto questionDto = QuestionDto.from(question);

        assertEquals(question.getId(), questionDto.id());
        assertEquals(question.getQuestionText(), questionDto.questionText());
        assertEquals(question.getLevel(), questionDto.level());
        assertEquals(question.getTestType().getId(), questionDto.testTypeId());
    }

    @Test
    @DisplayName("Test conversion from Question object with null TestType and Answers fields")
    void shouldReturnNullWhenQuestionIsNull() {

        QuestionDto result = QuestionDto.from((Question) null);

        assertNull(result);
    }

    @Test
    void shouldBeValidWithCorrectData() {

        QuestionDto dto = new QuestionDto(1L, "What is an interface in Java?", 3, 5L);

        Set<ConstraintViolation<QuestionDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No violations should be present for a valid dto");
    }

    @Test
    void shouldBeInvalidWithBlankQuestionText() {

        QuestionDto dto = new QuestionDto(1L, "", 3, 5L);

        Set<ConstraintViolation<QuestionDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Violations should be present for a blank question text");

        String violationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        System.out.println("Violation messages: " + violationMessages);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("The question text must not be blank")), "There should be a violation for blank question text");
    }

    @Test
    void shouldBeInvalidWithNegativeLevel() {

        QuestionDto dto = new QuestionDto(1L, "What is an interface in Java?", -1, 5L); // Level is negative, should be positive

        Set<ConstraintViolation<QuestionDto>> violations = validator.validate(dto);


        assertFalse(violations.isEmpty(), "Violations should be present for a negative level");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("The level of the question must be a positive number")), "There should be a violation for negative level");
    }


}
