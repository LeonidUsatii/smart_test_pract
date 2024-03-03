package de.ait.smarttestit.dto.exam_task;

import de.ait.smarttestit.dto.test_type.TestTypeWithQuestionDto;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.models.TestType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

class ExamTaskWithTestTypeDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateDtoFromValidExamTask() {

        TestType testType = new TestType();
        ExamTask examTask = new ExamTask();
        examTask.setExamTaskTitle("Math Test");
        examTask.setTestTypes(Collections.singletonList(testType));
        examTask.setExam(new Exam());

        ExamTaskWithTestTypeDto dto = ExamTaskWithTestTypeDto.from(examTask, 1L);

        assertNotNull(dto);
        assertEquals(1, dto.testTypes().size());
        assertEquals(1L, dto.examId());
    }

    @Test
    void shouldHandleNullExamTask() {

        Long examId = 1L;

        assertThrows(NullPointerException.class, () -> {
            ExamTaskWithTestTypeDto.from(null, examId);
        });
    }

    @Test
    void shouldBeValidWithCorrectData() {

        List<TestTypeWithQuestionDto> testTypes = new ArrayList<>();
        Long examId = 1L;

        ExamTaskWithTestTypeDto dto = new ExamTaskWithTestTypeDto(testTypes, examId);
        Set<ConstraintViolation<ExamTaskWithTestTypeDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeInvalidWithNegativeExamId() {

        List<TestTypeWithQuestionDto> testTypes = new ArrayList<>();
        Long examId = -1L;

        ExamTaskWithTestTypeDto dto = new ExamTaskWithTestTypeDto(testTypes, examId);
        Set<ConstraintViolation<ExamTaskWithTestTypeDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
}



