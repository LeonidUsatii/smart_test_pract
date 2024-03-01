import de.ait.smarttestit.dto.exam.ExamDto;
import de.ait.smarttestit.models.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ExamDtoTest {

    @Test
    @DisplayName("Test conversion from null Exam object")
    void shouldReturnNullWhenFromIsCalledWithNull() {
        assertNull(ExamDto.from(null));
    }

    @Test
    @DisplayName("Test conversion from Exam object with all fields filled")
    void shouldReturnExamDtoWithAllFieldsFilledWhenFromIsCalledWithExam() {
        // Given
        Exam exam = new Exam();
        exam.setId(1L);
        exam.setExamScore(85);
        exam.setExamStartTime(LocalDateTime.parse("2022-01-01T09:00:00"));
        exam.setExamEndTime(LocalDateTime.parse("2022-01-01T12:00:00"));
        exam.setExamDuration(180);
        exam.setExamStatus(ExamStatus.COMPLETED);
        User user = new User();
        user.setId(2L);
        exam.setUser(user);
        Applicant applicant = new Applicant();
        applicant.setId(2L);
        exam.setApplicant(applicant);
        ExamTask examTask = new ExamTask();
        examTask.setId(3L);
        exam.setExamTask(examTask);

        // When
        ExamDto examDto = ExamDto.from(exam);

        // Then
        assertEquals(exam.getId(), examDto.id());
        assertEquals(exam.getExamScore(), examDto.examScore());
        assertEquals(exam.getExamStartTime().toString(), examDto.examStartTime());
        assertEquals(exam.getExamEndTime().toString(), examDto.examEndTime());
        assertEquals(exam.getExamDuration(), examDto.examDuration());
        assertEquals(exam.getExamStatus().toString(), examDto.examStatus());
        assertEquals(exam.getUser().getId(), examDto.userId());
        assertEquals(exam.getApplicant().getId(), examDto.applicantId());
        assertEquals(exam.getExamTask().getId(), examDto.examTaskId());
    }

    @Test
    @DisplayName("Field validation of ExamDto object")
    void shouldValidateFields() {
        // Given
        ExamDto examDto = new ExamDto(-1L,
                -1,
                "",
                "",
                -1,
                "",
                null,
                null,
                -1L);

        // Create validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // When
        Set<ConstraintViolation<ExamDto>> violations = validator.validate(examDto);

        Map<String, List<String>> errorMessages = violations.stream()
                .collect(Collectors.groupingBy(violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));

        // Then
        assertEquals(7, errorMessages.size());

        assertTrue(errorMessages.containsKey("examTaskId"));
        assertEquals(1, errorMessages.get("examTaskId").size());
        assertEquals("Exam task ID must be a positive number", errorMessages.get("examTaskId").get(0));

        assertTrue(errorMessages.containsKey("examEndTime"));
        assertEquals(1, errorMessages.get("examEndTime").size());
        assertEquals("Exam end time must not be blank", errorMessages.get("examEndTime").get(0));

        assertTrue(errorMessages.containsKey("examStatus"));
        assertEquals(1, errorMessages.get("examStatus").size());
        assertEquals("Exam status must not be blank", errorMessages.get("examStatus").get(0));

        assertTrue(errorMessages.containsKey("examScore"));
        assertEquals(1, errorMessages.get("examScore").size());
        assertEquals("Exam score must be zero or a positive number", errorMessages.get("examScore").get(0));

        assertTrue(errorMessages.containsKey("examStartTime"));
        assertEquals(1, errorMessages.get("examStartTime").size());
        assertEquals("Exam start time must not be blank", errorMessages.get("examStartTime").get(0));

        assertTrue(errorMessages.containsKey("id"));
        assertEquals(1, errorMessages.get("id").size());
        assertEquals("Exam ID must be a positive number", errorMessages.get("id").get(0));

        assertTrue(errorMessages.containsKey("examDuration"));
        assertEquals(1, errorMessages.get("examDuration").size());
        assertEquals("Exam duration must be a positive number", errorMessages.get("examDuration").get(0));
    }
}
