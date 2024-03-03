package de.ait.smarttestit.dto.test_type;

import de.ait.smarttestit.dto.question.QuestionWithAnswersDto;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class TestTypeWithQuestionDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateDtoFromValidTestType() {

        TestType testType = new TestType();
        testType.setName("Logic");
        Question question = new Question();
        testType.setQuestions(List.of(question));

        TestTypeWithQuestionDto dto = TestTypeWithQuestionDto.from(testType);

        assertNotNull(dto);
        assertEquals("Logic", dto.name());
        assertNotNull(dto.questions());
        assertEquals(1, dto.questions().size());
    }

    @Test
    void shouldHandleNullTestType() {

        TestType testType = null;

        assertThrows(NullPointerException.class, () -> TestTypeWithQuestionDto.from(testType));
    }

    @Test
    void shouldBeValidWithCorrectData() {

        String name = "Logic";
        List<QuestionWithAnswersDto> questions = new ArrayList<>();

        TestTypeWithQuestionDto dto = new TestTypeWithQuestionDto(name, questions);
        Set<ConstraintViolation<TestTypeWithQuestionDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No validation errors");
    }

    @Test
    void shouldBeInvalidWithBlankName() {

        String name = "";
        List<QuestionWithAnswersDto> questions = new ArrayList<>();

        TestTypeWithQuestionDto dto = new TestTypeWithQuestionDto(name, questions);
        Set<ConstraintViolation<TestTypeWithQuestionDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Validation error due to empty name");
        assertEquals(1, violations.size());
    }
}
