package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.*;
import de.ait.smarttestit.repositories.ApplicantRepository;
import de.ait.smarttestit.repositories.TokenRepository;
import de.ait.smarttestit.services.ApplicantService;
import de.ait.smarttestit.services.ExamService;
import de.ait.smarttestit.services.ExamTasksService;
import de.ait.smarttestit.services.TokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ApplicantRepository applicantRepository;
    private final ExamService examService;
    private final ApplicantService applicantService;
    private final ExamTasksService examTasksService;

    @Value("${tokenExpireDay}")
    private int tokenExpireDay;

    @Override
    public String generateApplicantToken(Long examId, Applicant applicant) {

        Exam exam = examService.getExamOrThrow(examId);

        String generatedToken = UUID.randomUUID().toString();
        LocalDateTime expireTime = LocalDateTime.now().plusDays(tokenExpireDay);
        Token token = new Token(generatedToken, expireTime, applicant,exam);
        Token savedToken = tokenRepository.save(token);

        return generatedToken;
    }

    @Override
    public String generateApplicantToken(NewApplicantTaskDto applicantTaskDto)  {

        Applicant applicant = applicantService.create(applicantTaskDto);
        ExamTask examTask = examTasksService.examTaskForExam(applicantTaskDto);
        examTasksService.save(examTask);
        Exam exam = new Exam(applicantTaskDto.examDuration(), ExamStatus.PLANNED, applicant, examTask);
        exam = examService.save(exam);
        return generateApplicantToken(exam.getId(), applicant);
    }

    @Override
    public void saveTokenCode(String codeValue, Applicant applicant, ExamTask examTask) {
        Token token = new Token();
        token.setCode(codeValue);
        token.setApplicant(applicant);
        token.setExpiredDateTime(LocalDateTime.now().plusMinutes(10));
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public ApplicantDto tokenCheck(String tokenCode) {
        Token token = tokenRepository
                .findByCodeAndExpiredDateTimeAfter(tokenCode, LocalDateTime.now())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Token code not found or is expired"));

        Applicant applicant = applicantRepository
                .findFirstByTokensCode(token.getCode())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Applicant by that token not found"));

        applicantRepository.save(applicant);

        return ApplicantDto.from(applicant);
    }

    @Override
    public Token getByCodeOrThrow(String tokenCode) {
        return tokenRepository.findByCode(tokenCode)
                .orElseThrow(() -> new EntityNotFoundException("Token not found with code: " + tokenCode));
    }
}