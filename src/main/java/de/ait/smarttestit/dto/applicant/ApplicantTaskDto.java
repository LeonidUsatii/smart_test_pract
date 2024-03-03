package de.ait.smarttestit.dto.applicant;

import de.ait.smarttestit.dto.exam_task.NewTestsParamDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(name = "ApplicantTaskDto", description = "Representation of a information about applicant," +
        " examTitle for  applicant and examTaskList")
public record ApplicantTaskDto(@NotNull(message = "The id must not be null")
                               @Positive(message = "The id must be positive")
                               @Schema(description = "Applicant_ID", example = "1")
                               Long id,

                               @NotBlank
                               @Schema(description = "Exam title", example = "Exam1")
                               String examTitle,

                               @Schema(description = "Exam duration", example = "120")
                               int examDuration,

                               @Schema(description = "List of ExamTask", example = """
                                       [
                                         {
                                          "testTypeId": 1,
                                           "questionsLevel": 2,
                                           "questionsCount": 2
                                         },
                                         {
                                           "testTypeId": 2,
                                           "questionsLevel": 1,
                                           "questionsCount": 2
                                         }
                                       ]""")
                               List<NewTestsParamDto> examTaskDtoList,

                               @Schema(description = "Information of applicant",
                                       example = """
                                                {
                                                   "firstName": "Ivan",
                                                   "lastName": "Ivanov",
                                                   "email": "ivanov@gmail.com",
                                                   "address": "Berlin",
                                                   "phoneNumber": "0123456789"
                                                 }
                                               """)
                               NewApplicantDto applicantInfo) {
}