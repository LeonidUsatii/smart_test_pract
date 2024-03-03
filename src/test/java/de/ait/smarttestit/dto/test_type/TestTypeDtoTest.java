package de.ait.smarttestit.dto.test_type;

import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.models.TestType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class TestTypeDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldConvertFromTestTypeSuccessfully() {

        TestType testType = new TestType();
        testType.setId(1L);
        testType.setName("Sample Test Type");
        ExamTask examTask = new ExamTask();
        examTask.setId(2L);
        testType.setExamTask(examTask);

        TestTypeDto result = TestTypeDto.from(testType);

        assertEquals(testType.getId(), result.id());
        assertEquals(testType.getName(), result.name());
        assertEquals(testType.getExamTask().getId(), result.examTaskId());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenTestTypeIsNull() {
        assertThrows(NullPointerException.class, () -> {
            TestTypeDto.from((TestType) null);
        });
    }

    @Test
    void shouldBeValidWithCorrectData() {

        TestTypeDto dto = new TestTypeDto(1L, "Sample Test Type", 2L);

        Set<ConstraintViolation<TestTypeDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "No violations should be present for a valid dto");
    }

    @Test
    void shouldBeInvalidWithBlankName() {

        TestTypeDto dto = new TestTypeDto(1L, "", 2L);

        Set<ConstraintViolation<TestTypeDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Violations should be present for a blank name");
        assertEquals(1, violations.size(), "There should be one violation for a blank name");
    }

    @Test
    void shouldBeInvalidWithNullId() {

        TestTypeDto dto = new TestTypeDto(null, "Sample Test Type", 2L);

        Set<ConstraintViolation<TestTypeDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Violations should be present for a null id");
        assertEquals(1, violations.size(), "There should be one violation for a null id");
    }
}
