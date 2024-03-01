package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantDto;
import de.ait.smarttestit.dto.applicant.UpdateApplicantDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.repositories.ApplicantRepository;
import de.ait.smarttestit.services.ApplicantService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.ait.smarttestit.dto.applicant.ApplicantDto.from;

@AllArgsConstructor
@Service
@Component
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;

    @Override
    public Applicant getApplicantOrThrow(@NonNull Long applicantId) {
        return applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Applicant with id <" + applicantId + "> not found"));
    }

    @Override
    public ApplicantDto addApplicant(@NonNull NewApplicantDto newApplicantDto) {
        if (applicantRepository.existsByEmail(newApplicantDto.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT,
                    "Applicant with email <" + newApplicantDto.getEmail() + "> already exists");
        }
        Applicant newApplicant = new Applicant(
                newApplicantDto.getFirstName(),
                newApplicantDto.getLastName(),
                newApplicantDto.getEmail(),
                newApplicantDto.getAddress(),
                newApplicantDto.getPhoneNumber()
        );
        newApplicant = applicantRepository.save(newApplicant);

        return from(newApplicant);
    }

    @Override
    public List<ApplicantDto> getListApplicants() {
        return applicantRepository.findAll().stream()
                .map(ApplicantDto::from)
                .toList();
    }

    @Override
    public ApplicantDto deleteApplicant(@NonNull Long applicantId) {
        Applicant applicant = getApplicantOrThrow(applicantId);
        applicantRepository.delete(applicant);

        return ApplicantDto.from(applicant);
    }

    @Override
    public ApplicantDto updateApplicant(@NonNull Long applicantId, @NonNull UpdateApplicantDto updateApplicant) {

        Applicant applicant = getApplicantOrThrow(applicantId);
        applicant.setFirstName(updateApplicant.getFirstName());
        applicant.setLastName(updateApplicant.getLastName());
        applicant.setEmail(updateApplicant.getEmail());
        applicant.setAddress(updateApplicant.getAddress());
        applicant.setPhoneNumber(updateApplicant.getPhoneNumber());

        applicant = applicantRepository.save(applicant);

        return ApplicantDto.from(applicant);
    }

    @Override
    public ApplicantDto getApplicant(@NonNull Long applicantId) {

        Applicant applicant = getApplicantOrThrow(applicantId);

        return ApplicantDto.from(applicant);
    }

    @Override
    public List<Exam> addExamToApplicant(@NonNull Long applicantId, @NonNull Exam exam) {

        Applicant applicant = getApplicantOrThrow(applicantId);

        List<Exam> updatedExams = applicant.getExams();

        updatedExams.add(exam);

        applicant.setExams(updatedExams);

        applicantRepository.save(applicant);

        return updatedExams;
    }
}
