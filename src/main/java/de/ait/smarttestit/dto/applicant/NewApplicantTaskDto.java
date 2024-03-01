package de.ait.smarttestit.dto.applicant;

import de.ait.smarttestit.dto.exam_task.NewTestsParamDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public record NewApplicantTaskDto(@NotBlank
                                  @Schema(description = "Exam title", example = "Exam1")
                                  String examTitle,

                                  @Schema(description = "Exam duration", example = "30")
                                  int examDuration,

                                  @Schema(description = "List of ExamTask",
                                          example = """
                                                  [
                                                    {
                                                     "testTypeId": 1,
                                                      "questionsLevel": 2,
                                                      "questionsCount": 10
                                                    },
                                                    {
                                                      "testTypeId": 2,
                                                      "questionsLevel": 1,
                                                      "questionsCount": 8
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
                                  NewApplicantDto applicantInfo) implements Serializable {
}
