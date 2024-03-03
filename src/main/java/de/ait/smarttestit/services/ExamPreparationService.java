package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.applicant.InfoAboutApplicantForExamDto;

public interface ExamPreparationService {
    InfoAboutApplicantForExamDto getInfoAboutApplicantForExam(String tokenCode);
}
