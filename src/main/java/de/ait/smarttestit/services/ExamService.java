package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.exam.ExamDto;
import de.ait.smarttestit.dto.exam.NewExamDto;
import de.ait.smarttestit.dto.exam.UpdateExamDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Exam;
import lombok.NonNull;

import java.util.List;

public interface ExamService {

    /**
     * Retrieves a {@link Exam} by its ID or throws a {@link RestException} with status NOT_FOUND if not found.
     *
     * @param examId The ID of the applicant.
     * @return The {@link Exam} object.
     * @throws RestException If the exam with the specified ID is not found.
     */
    Exam getExamOrThrow(@NonNull final Long examId);

    /**
     * Add a new applicant.
     *
     * @param newExamDto The new exam to be added.
     * @return The created exam.
     */
    ExamDto addExam(@NonNull final NewExamDto newExamDto);

    /**
     * Retrieves all exams.
     *
     * @return A list of {@link ExamDto} objects representing the exams.
     */
    List<ExamDto> getListExams();

    /**
     * Deletes an exam by its ID.
     *
     * @param examId The ID of the exam to delete.
     * @return The deleted exam represented by a {@link ExamDto} object.
     */
    ExamDto deleteExam(@NonNull final Long examId);

    /**
     * Updates an exam with the specified ID.
     *
     * @param examId     The ID of the exam to update.
     * @param updateExam The updated exam data.
     * @return The updated exam data.
     */
    ExamDto updateExam(@NonNull final Long examId, @NonNull final UpdateExamDto updateExam);

    /**
     * Retrieves exam by ID.
     *
     * @param examId     The ID of the exam.
     * @return A  {@link ExamDto} object.
     */
    ExamDto getExam(@NonNull final Long examId);

    /**
     * Saving exam  data.
     *
     * @param exam     The exam information.
     * @return A  {@link Exam} object.
     */
    Exam save(Exam exam);
}