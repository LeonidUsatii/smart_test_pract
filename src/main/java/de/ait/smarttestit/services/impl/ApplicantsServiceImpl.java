package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantDto;
import de.ait.smarttestit.dto.applicant.UpdateApplicantDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.repositories.ApplicantsRepository;
import de.ait.smarttestit.services.ApplicantsService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static de.ait.smarttestit.dto.applicant.ApplicantDto.from;

@AllArgsConstructor
@Service
@Component
public class ApplicantsServiceImpl implements ApplicantsService {

    private final ApplicantsRepository applicantsRepository;

    @Override
    public Applicant getApplicantOrThrow(@NonNull Long applicantId) {
        return applicantsRepository.findById(applicantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Applicant with id <" + applicantId + "> not found"));
    }

    @Override
    public ApplicantDto addApplicant(@NonNull NewApplicantDto newApplicantDto) {
        if (applicantsRepository.existsByEmail(newApplicantDto.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT,
                    "Applicant with email <" + newApplicantDto.getEmail() + "> already exists");
        }
        Applicant applicant = new Applicant(
                newApplicantDto.getFirstName(),
                newApplicantDto.getLastName(),
                newApplicantDto.getEmail(),
                newApplicantDto.getHashPassword(),
                newApplicantDto.getAddress(),
                newApplicantDto.getPhoneNumber()
        );
        applicant = applicantsRepository.save(applicant);

        return from(applicant);
    }

    @Override
    public List<ApplicantDto> getListApplicants() {
        List<Applicant> applicants = applicantsRepository.findAll();
        if (applicants != null) {
            return applicants.stream()
                    .map(ApplicantDto::from)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ApplicantDto deleteApplicant(@NonNull Long applicantId) {
        Applicant applicant = getApplicantOrThrow(applicantId);
        applicantsRepository.delete(applicant);

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

        applicant = applicantsRepository.save(applicant);

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

        applicantsRepository.save(applicant);

        return updatedExams;
    }
}
