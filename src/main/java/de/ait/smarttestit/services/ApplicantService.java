package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.applicant.ApplicantDto;
import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.dto.applicant.UpdateApplicantDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Applicant;
import de.ait.smarttestit.models.Exam;
import lombok.NonNull;

import java.util.List;

public interface ApplicantService {

    /**
     * Retrieves a {@link Applicant} by its ID or throws a {@link RestException} with status NOT_FOUND if not found.
     *
     * @param applicantId The ID of the applicant.
     * @return The {@link Applicant} object.
     * @throws RestException If the applicant with the specified ID is not found.
     */
    Applicant getApplicantOrThrow(@NonNull final Long applicantId);

    /**
     * Add a new applicant.
     *
     * @param newApplicantDto The new applicant to be added.
     * @return The created applicant.
     */
    ApplicantDto addApplicant(final NewApplicantTaskDto newApplicantDto);

    /**
     * Retrieves all applicants.
     *
     * @return A list of {@link ApplicantDto} objects representing the applicants.
     */
    List<ApplicantDto> getListApplicants();

    /**
     * Deletes an applicant by its ID.
     *
     * @param applicantId The ID of the applicant to delete.
     * @return The deleted applicant represented by a {@link ApplicantDto} object.
     */
    ApplicantDto deleteApplicant(@NonNull final Long applicantId);

    /**
     * Updates an applicant with the specified ID.
     *
     * @param applicantId     The ID of the applicant to update.
     * @param updateApplicant The updated applicant data.
     * @return The updated applicant data.
     */
    ApplicantDto updateApplicant(@NonNull final Long applicantId, @NonNull final UpdateApplicantDto updateApplicant);

    /**
     * Retrieves applicant by ID.
     *
     * @param applicantId     The ID of the applicant.
     * @return A  {@link ApplicantDto} object.
     */
    ApplicantDto getApplicant(@NonNull final Long applicantId);

    /**
     * Creates an exam for the applicant by ID.
     *
     * @param applicantId     The ID of the applicant.
     * @param exam     The exam.
     * @return A  {@link Exam} object.
     */
    List<Exam> addExamToApplicant(@NonNull final Long applicantId, @NonNull final Exam exam);

    /**
     * Creating an applicant based on the provided data.
     *
     * @param applicantTaskDto     The applicant information.
     * @return A  {@link Applicant} object.
     */
    Applicant create(@NonNull final NewApplicantTaskDto applicantTaskDto);

    /**
     * Saving applicant data.
     *
     * @param applicant     The applicant information.
     * @return A  {@link Applicant} object.
     */
    Applicant save(Applicant applicant);
}