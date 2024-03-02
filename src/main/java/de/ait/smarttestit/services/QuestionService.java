package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.dto.question.NewQuestionDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.dto.question.UpdateQuestionDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import lombok.NonNull;

import java.util.List;

public interface QuestionService {
    /**
     * Add a new question.
     *
     * @param newQuestion The new question to be added.
     * @return The created question.
     */
    QuestionDto addQuestion(@NonNull final Long testTypeId,@NonNull final NewQuestionDto newQuestion);

    /**
     * Creating test type with questions.
     *
     * @param testTypeId The ID of the test type.
     * @return The {@link TestType} object.
     */
    TestType createTestTypeForExam(Long testTypeId,
                                   int questionLevel, int questionCount);

    /**
     * Creating list of test types
     *
     * @param applicantTaskDto The data of ApplicantTaskDto classes.
     * @return The list of {@link TestType} objects.
     */
    List<TestType> createTestTypesForExam(NewApplicantTaskDto applicantTaskDto);

    /**
     * Updates a question with the specified ID.
     *
     * @param questionId     The ID of the question to update.
     * @param updateQuestion The updated question data.
     * @return The updated question data.
     */
    QuestionDto updateQuestion(@NonNull final Long testType,
                               @NonNull final Long questionId,
                               @NonNull final UpdateQuestionDto updateQuestion);

    /**
     * Deletes a question by its ID.
     *
     * @param questionId The ID of the question to delete.
     * @return The deleted question represented by a {@link QuestionDto} object.
     */
    QuestionDto deleteQuestion(@NonNull final Long questionId);

    /**
     * Retrieves all questions.
     *
     * @return A list of {@link QuestionDto} objects representing the questions.
     */
    List<QuestionDto> getAll();

    /**
     * Retrieves a {@link Question} by its ID or throws a {@link RestException} with status NOT_FOUND if not found.
     *
     * @param questionId The ID of the question.
     * @return The {@link Question} object.
     * @throws RestException If the test type with the specified ID is not found.
     */
    Question getByIdOrThrow(@NonNull final Long questionId);

    /**
     * Selection of a certain amount of questions by test type and level.
     *
     * @param testTypeId     The ID of the test type.
     * @param questionLevel The level of questions.
     * @param questionCount The number of questions to be selected.
     * @return The certain number of questions of a certain test type and level.
     */
    List<Question> getListQuestionsForTestType(Long testTypeId,int questionLevel, int questionCount);
}