package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.ApplicantsApi;
import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.services.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApplicantsController implements ApplicantsApi {

    private final ApplicantService applicantService;

    @Override
    public ApplicantDto addApplicant(NewApplicantTaskDto newApplicantDto) {
        return applicantService.addApplicant(newApplicantDto);
    }
}
