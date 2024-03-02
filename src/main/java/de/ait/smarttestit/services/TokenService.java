package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.ExamTask;

public interface TokenService {

    /**
     * Saving token code.
     *
     * @param applicant     The applicant information.
     * @param codeValue     The token code.
     * @param examTask     The exam task for applicant.
     * @return Saved token object.
     */
    void saveTokenCode(String codeValue, Applicant applicant, ExamTask examTask);

    /**
     * Search applicant by token code.
     *
     * @param tokenCode     The token code.
     * @return The applicant information.
     */
    ApplicantDto tokenCheck(String tokenCode);

    /**
     * Creating a token code.
     *
     * @param examId     The exam details.
     * @param applicant     The applicant information.
     * @return The generated token code.
     */
    String generateApplicantToken(Long examId, Applicant applicant);

    /**
     * Creating a token based on ApplicantTaskDto data.
     *
     * @param applicantTaskDto     The applicantTask details.
     * @return The generated token code.
     */
     String generateApplicantToken(NewApplicantTaskDto applicantTaskDto);
}