package de.ait.smarttestit.dto.applicant;

import de.ait.smarttestit.models.Applicant;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Andrej Reutow
 * created on 29.02.2024
 */
class ApplicantDtoTest {

    @Test
    @DisplayName("Test conversion from null Applicant object")
    void shouldReturnNullWhenFromIsCalledWithNull() {
        assertNull(ApplicantDto.from(null));
    }

    @Test
    @DisplayName("Test conversion from Applicant object with all fields filled")
    void shouldReturnApplicantDtoWithAllFieldsFilledWhenFromIsCalledWithApplicant() {

        Applicant applicant = new Applicant();
        applicant.setId(1L);
        applicant.setFirstName("John");
        applicant.setLastName("Doe");
        applicant.setEmail("john.doe@example.com");
        applicant.setAddress("123 Street, City");
        applicant.setPhoneNumber("+12345678");

        ApplicantDto applicantDto = ApplicantDto.from(applicant);

        assertEquals(applicant.getId(), applicantDto.getId());
        assertEquals(applicant.getFirstName(), applicantDto.getFirstName());
        assertEquals(applicant.getLastName(), applicantDto.getLastName());
        assertEquals(applicant.getEmail(), applicantDto.getEmail());
        assertEquals(applicant.getAddress(), applicantDto.getAddress());
        assertEquals(applicant.getPhoneNumber(), applicantDto.getPhoneNumber());
    }

    @Test
    @DisplayName("Field validation of ApplicantDto object")
    void shouldValidateFields() {

        ApplicantDto applicantDto = new ApplicantDto(-1L, "", "", "", "", "");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<ApplicantDto>> violations = validator.validate(applicantDto);

        Map<String, List<String>> errorMessages = violations.stream()
                .collect(Collectors.groupingBy(violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));

        assertEquals(4, errorMessages.size());

        assertTrue(errorMessages.containsKey("id"));
        assertEquals(1, errorMessages.get("id").size());

        assertTrue(errorMessages.containsKey("firstName"));
        assertEquals(1, errorMessages.get("firstName").size());

        assertTrue(errorMessages.containsKey("lastName"));
        assertEquals(1, errorMessages.get("lastName").size());

        assertTrue(errorMessages.containsKey("email"));
        assertEquals(1, errorMessages.get("email").size());
    }
}
