package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.InfoAboutApplicantForExamDto;
import de.ait.smarttestit.models.Token;
import de.ait.smarttestit.services.ExamPreparationService;
import de.ait.smarttestit.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExamPreparationImpl implements ExamPreparationService {

    private final TokenService tokenService;
    @Override
    public InfoAboutApplicantForExamDto getInfoAboutApplicantForExam(String tokenCode) {

        Token token = tokenService.getByCodeOrThrow(tokenCode);

        return InfoAboutApplicantForExamDto.from(token);
    }
}
