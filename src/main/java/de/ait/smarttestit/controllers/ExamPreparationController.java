package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.ExamPreparationApi;
import de.ait.smarttestit.dto.applicant.InfoAboutApplicantForExamDto;
import de.ait.smarttestit.services.ExamPreparationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ExamPreparationController implements ExamPreparationApi {

    private final ExamPreparationService examPreparationService;

    @Override
    public InfoAboutApplicantForExamDto examPreparation(String tokenCode) {

        return examPreparationService.getInfoAboutApplicantForExam(tokenCode);
    }
}
