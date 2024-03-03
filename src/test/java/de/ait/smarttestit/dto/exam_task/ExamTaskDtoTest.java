package de.ait.smarttestit.dto.exam_task;

import de.ait.smarttestit.models.ExamTask;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.ait.smarttestit.dto.exam_task.ExamTaskDto.from;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Andrej Reutow
 * created on 29.02.2024
 */
class ExamTaskDtoTest {


    private final SpringValidatorAdapter validator = new LocalValidatorFactoryBean();

    @Test
    @DisplayName("When method 'from' is called with null")
    void shouldReturnNull_whenFromCalledWithNull() {
        ExamTask examTask = null;
        assertNull(from(examTask));
    }

    @Test
    @DisplayName("When method 'from' is called with exam task")
    void shouldReturnExamTaskDto_whenFromCalledWithExamTask() {

        ExamTask examTask = new ExamTask();
        examTask.setId(1L);
        examTask.setExamTaskTitle("Mocked Title");

        ExamTaskDto examTaskDto = from(examTask);

        assertEquals(examTask.getId(), examTaskDto.id());
        assertEquals(examTask.getExamTaskTitle(), examTaskDto.examTaskTitle());
    }

    @Test
    @DisplayName("Test validation of fields in ExamTaskDto")
    void validationShouldDetectInvalidFields() {

        ExamTaskDto examTaskDto = new ExamTaskDto(-1L, "");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<ExamTaskDto>> violations = validator.validate(examTaskDto);

        assertEquals(3, violations.size());
        Map<String, List<String>> groupedViolations = violations.stream()
                .collect(Collectors.groupingBy(violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));

        assertTrue(groupedViolations.containsKey("id"));
        List<String> invalIdFields = groupedViolations.get("id");
        assertEquals(1, invalIdFields.size());
        assertEquals("ID must be a positive number", invalIdFields.get(0));

        assertTrue(groupedViolations.containsKey("examTaskTitle"));
        List<String> invalTitleFields = groupedViolations.get("examTaskTitle");
        assertEquals(2, invalTitleFields.size());
        assertTrue(invalTitleFields.contains("Test title must be between 1 and 255 characters"),
                "Validation errors for filed examTaskTitle: " + invalTitleFields);
        assertTrue(invalTitleFields.contains("Test title is required"),
                "Validation errors for filed examTaskTitle: " + invalTitleFields);
    }
}
