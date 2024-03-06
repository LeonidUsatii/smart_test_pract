package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.answer.AnswerDto;
import de.ait.smarttestit.dto.answer.NewAnswerDto;
import de.ait.smarttestit.dto.answer.UpdateAnswerDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Answer;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.NonNull;

import java.util.List;

@Hidden
public interface AnswerService {

    /**
     * Add a new answer.
     *
     * @param newAnswer The new answer to be added.
     * @return The created answer.
     */
    AnswerDto addAnswer(@NonNull final Long questionId, @NonNull final NewAnswerDto newAnswer);

    /**
     * Retrieves a {@link Answer} by its ID or throws a {@link RestException} with status NOT_FOUND if not found.
     *
     * @param answerId The ID of the answer.
     * @return The {@link Answer} object.
     * @throws RestException If the answer with the specified ID is not found.
     */
    Answer getByIdOrThrow(@NonNull final Long answerId);

    /**
     * Retrieves all answers.
     *
     * @return A list of {@link AnswerDto} objects representing the answers.
     */
    List<AnswerDto> getAll();

    /**
     * Updates an answer with the specified ID.
     *
     * @param answerId     The ID of the answer to update.
     * @param updateAnswer The updated answer data.
     * @return The updated answer data.
     */
    AnswerDto updateAnswer(@NonNull final Long questionId,
                           @NonNull final Long answerId,
                           @NonNull final UpdateAnswerDto updateAnswer);

    /**
     * Deletes an answer by its ID.
     *
     * @param answerId The ID of the answer to delete.
     * @return The deleted answer represented by a {@link QuestionDto} object.
     */
    AnswerDto deleteAnswer(@NonNull final Long answerId);
}
