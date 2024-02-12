package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantDto;
import de.ait.smarttestit.dto.applicant.UpdateApplicantDto;
import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import lombok.NonNull;
import java.util.List;

public interface ApplicantsService {

    Applicant getApplicantOrThrow(@NonNull final Long applicantId);

    ApplicantDto addApplicant(@NonNull final NewApplicantDto newApplicantDto);

    List<ApplicantDto> getListApplicants();

    ApplicantDto deleteApplicant(@NonNull final Long applicantId);

    ApplicantDto updateApplicant(@NonNull final Long applicantId, @NonNull final UpdateApplicantDto updateApplicant);

    ApplicantDto getApplicant(@NonNull final Long applicantId);

    List<Exam> addExamToApplicant(@NonNull final Long applicantId, @NonNull final Exam exam);
}
