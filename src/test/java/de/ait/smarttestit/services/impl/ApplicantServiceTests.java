package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantDto;
import de.ait.smarttestit.dto.applicant.UpdateApplicantDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.repositories.ApplicantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Applicant CRUD:")
public class ApplicantServiceTests {

    private static final Long APPLICANT_ID = 1L;
    private static final List<Exam> EXAMS = new ArrayList<>();
    private static final Applicant APPLICANT = new Applicant(APPLICANT_ID, "Applicant", "Applicantov",
            "applicantov@mail.com", "123 Main St", "555-1234", EXAMS,null);

    private static final NewApplicantDto NEW_APPLICANT_DTO = new NewApplicantDto("Applicant", "Applicantov",
            "applicantov@mail.com", "123 Main St", "555-1234");

    @Mock
    private ApplicantRepository applicantRepository;

    @InjectMocks
    private ApplicantServiceImpl applicantsServices;

    @Nested
    @DisplayName("get applicant or trow")
    class GetApplicantOrThrow {

        @Test
        void getExamOrThrowPositive() {

            when(applicantRepository.findById(APPLICANT_ID)).thenReturn(Optional.of(APPLICANT));

            Applicant foundExam = applicantsServices.getApplicantOrThrow(APPLICANT_ID);

            assertNotNull(foundExam, "Applicant should not be null");
            assertEquals(APPLICANT_ID, foundExam.getId(), "Applicant ID should match the requested ID");
        }

        @Test
        void getExamOrThrowNegative() {
            when(applicantRepository.findById(APPLICANT_ID)).thenReturn(Optional.empty());

            RestException thrown = assertThrows(RestException.class, () -> applicantsServices.getApplicantOrThrow(APPLICANT_ID),
                    "Expected getApplicantOrThrow to throw, but it didn't");

            assertTrue(thrown.getMessage().contains("Applicant with id <" + APPLICANT_ID + "> not found"), "Exception message should contain the correct applicant ID");
        }
    }

 /*   @Nested
    @DisplayName("add applicant")
    class AddApplicant {

        @Test
        void testAddApplicantPositive() {

            when(applicantRepository.existsByEmail(anyString())).thenReturn(false);
            when(applicantRepository.save(any(Applicant.class))).thenReturn(APPLICANT);

            ApplicantDto result = applicantsServices.addApplicant(NEW_APPLICANT_DTO);

            assertNotNull(result);
            assertEquals(APPLICANT.getId(), result.getId());
            verify(applicantRepository, times(1)).save(any(Applicant.class));
        }

        @Test
        void testAddApplicantNegative() {

            when(applicantRepository.existsByEmail(anyString())).thenReturn(true);

            RestException exception = assertThrows(RestException.class, () -> applicantsServices.addApplicant(NEW_APPLICANT_DTO));

            assertEquals(HttpStatus.CONFLICT, exception.getStatus());
            assertEquals("Applicant with email <applicantov@mail.com> already exists", exception.getMessage());
        }
    }*/

    @Nested
    @DisplayName("get list applicants")
    class GetListApplicants {

        @Test
        void testGetListApplicantsPositive() {

            Applicant applicant1 = new Applicant(1L, "Ivan", "Ivanov", "ivan@example.com", "Address 1", "1234567890");
            Applicant applicant2 = new Applicant(2L, "Petr", "Petrov", "petr@example.com", "Address 2", "0987654321");
            when(applicantRepository.findAll()).thenReturn(Arrays.asList(applicant1, applicant2));

            List<ApplicantDto> result = applicantsServices.getListApplicants();

            assertEquals(2, result.size());
        }

        @Test
        void testGetListApplicantsNegative() {

            when(applicantRepository.findAll()).thenReturn(Collections.emptyList());

            List<ApplicantDto> result = applicantsServices.getListApplicants();

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("get applicant")
    class GetExam {

        @Test
        void testGetApplicantPositive() {

            when(applicantRepository.findById(APPLICANT_ID)).thenReturn(Optional.of(APPLICANT));

            ApplicantDto result = applicantsServices.getApplicant(APPLICANT_ID);

            assertEquals(APPLICANT_ID, result.getId());
            assertEquals("Applicant", result.getFirstName());
            assertEquals("Applicantov", result.getLastName());
        }

        @Test
        void testGetApplicantNegative() {

            Long invalidApplicantId = 5L;

            RestException exception = assertThrows(RestException.class,
                    () -> applicantsServices.getApplicant(invalidApplicantId));

            assertThat(exception.getMessage().trim(),
                    containsString("Applicant with id <" + invalidApplicantId + "> not found"));
        }
    }

    @Nested
    @DisplayName("delete applicant")
    class deleteApplicant {

        @Test
        void testDeleteApplicantPositive() {

            when(applicantRepository.findById(APPLICANT_ID)).thenReturn(Optional.of(APPLICANT));
            doNothing().when(applicantRepository).delete(any(Applicant.class));

            ApplicantDto result = applicantsServices.deleteApplicant(APPLICANT_ID);

            assertNotNull(result);
            assertEquals(APPLICANT_ID, result.getId());
            verify(applicantRepository).delete(APPLICANT);
        }

        @Test
        void testDeleteApplicantNegative() {

            Long invalidApplicantId = 99L;

            when(applicantRepository.findById(invalidApplicantId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> applicantsServices.deleteApplicant(invalidApplicantId));

            verify(applicantRepository, never()).delete(any(Applicant.class));
        }
    }

    @Nested
    @DisplayName("update applicant")
    class updateApplicant {

        @Test
        void testUpdateApplicantPositive() {

            Long applicantId = 1L;
            UpdateApplicantDto updateDto = new UpdateApplicantDto("UpdatedFirstName", "UpdatedLastName",
                    "updated@example.com", "updatedOriginalAddress", "+1234567890");
            Applicant existingApplicant = new Applicant(applicantId, "OriginalFirstName", "OriginalLastName",
                    "original@example.com", "originalAddress", "+0987654321");
            when(applicantRepository.findById(applicantId)).thenReturn(Optional.of(existingApplicant));
            when(applicantRepository.save(any(Applicant.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ApplicantDto result = applicantsServices.updateApplicant(applicantId, updateDto);

            assertEquals("UpdatedFirstName", result.getFirstName());
            assertEquals("updated@example.com", result.getEmail());
            verify(applicantRepository).save(any(Applicant.class));
        }

        @Test
        void testUpdateApplicantNegative() {

            Long nonExistentApplicantId = 99L;
            UpdateApplicantDto updateDto = new UpdateApplicantDto("UpdatedFirstName", "UpdatedLastName",
                    "updated@example.com", "updatedOriginalAddress", "+1234567890");
            when(applicantRepository.findById(nonExistentApplicantId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> applicantsServices.updateApplicant(nonExistentApplicantId, updateDto));
            verify(applicantRepository, never()).save(any(Applicant.class));
        }
    }

    @Nested
    @DisplayName("add exam to applicant")
    class AddExamToApplicant {

        @Test
        void testAddExamToApplicantPositive() {

            Exam exam = new Exam();
            List<Exam> existingExams = new ArrayList<>();

            Applicant applicant = mock(Applicant.class);

            when(applicant.getExams()).thenReturn(existingExams);
            when(applicantRepository.findById(APPLICANT_ID)).thenReturn(Optional.of(applicant));

            List<Exam> updatedExams = applicantsServices.addExamToApplicant(APPLICANT_ID, exam);

            verify(applicant).setExams(anyList());
            verify(applicantRepository).save(applicant);
            assertTrue(updatedExams.contains(exam));
        }

        @Test
        void testAddExamToApplicantNegative() {

            Exam exam = new Exam();

            when(applicantRepository.findById(APPLICANT_ID)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> applicantsServices.addExamToApplicant(APPLICANT_ID, exam));
            verify(applicantRepository, never()).save(any(Applicant.class));
        }
    }
}
