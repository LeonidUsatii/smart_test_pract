package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.dto.exam_task.ExamTaskDto;
import de.ait.smarttestit.dto.exam_task.UpdateExamTaskDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.ExamTask;
import lombok.NonNull;

import java.util.List;

public interface ExamTasksService {

    /**
     * Retrieves a {@link ExamTask} by its ID or throws a {@link RestException} with status NOT_FOUND if not found.
     *
     * @param testId The ID of the applicant.
     * @return The {@link ExamTask} object.
     * @throws RestException If the exam task with the specified ID is not found.
     */
    ExamTask getByIdOrThrow(@NonNull final Long testId);

    /**
     * Confirmation of the uniqueness of the name of exam tasks.
     *
     * @param examTaskTitle The name of exam task.
     * @return True oder false.
     */
    boolean isExamTaskTitleUnique(String examTaskTitle);

    /**
     * Creating an exam task.
     *
     * @param applicantTaskDto The applicant information and exam task data.
     * @return Created exam task for applicant.
     */
    ExamTask examTaskForExam(NewApplicantTaskDto applicantTaskDto);

    /**
     * Retrieves all the exam tasks.
     *
     * @return A list of {@link ExamTaskDto} objects representing the exam tasks.
     */
    List<ExamTaskDto> getListExamTasks();

    /**
     * Deletes an exam task by its ID.
     *
     * @param examId The ID of the test type to delete.
     * @return The deleted exam task represented by a {@link ExamTaskDto} object.
     */
    ExamTaskDto deleteExamTask(@NonNull final Long examId);

    /**
     * Updates an exam task with the specified ID.
     *
     * @param examId     The ID of the exam task to update.
     * @param updateExamTask The updated test type data.
     * @return The updated exam task data.
     */
    ExamTaskDto updateExamTask(@NonNull final Long examId, @NonNull final UpdateExamTaskDto updateExamTask);

    /**
     * Saving exam task data.
     *
     * @param examTask     The exam task information.
     * @return A  {@link ExamTask} object.
     */
    ExamTask save(ExamTask examTask);
}