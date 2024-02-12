package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.answer.AnswerDto;
import de.ait.smarttestit.dto.answer.NewAnswerDto;
import de.ait.smarttestit.dto.answer.UpdateAnswerDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Answer;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.repositories.AnswersRepository;
import de.ait.smarttestit.services.AnswerService;
import de.ait.smarttestit.services.QuestionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Component
public class AnswerServiceImpl implements AnswerService {
    private final AnswersRepository answersRepository;
    private final QuestionService questionService;

    @Override
    @Transactional
    public AnswerDto addAnswer(@NonNull final Long questionId, @NonNull final NewAnswerDto newAnswer) {
        Question question = questionService.getByIdOrThrow(questionId);

        if (answersRepository.existsByAnswerTextAndQuestionId(newAnswer.answerText(), questionId)) {
            throw new RestException(HttpStatus.CONFLICT,
                    "Answer < " + newAnswer + "> already exists");
        }

        Answer answer = new Answer();
        answer.setAnswerText(newAnswer.answerText());
        answer.setCorrect(newAnswer.isCorrect());
        answer = answersRepository.save(answer);
        return AnswerDto.from(answer);
    }

    @Override
    public List<AnswerDto> getAll() {
        return answersRepository.findAll().stream()
                .map(AnswerDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public Answer getByIdOrThrow(@NonNull final Long answerId) {
        return answersRepository.findById(answerId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Answer with id <" + answerId + "> not found"));
    }

    @Override
    public AnswerDto updateAnswer(@NonNull final Long questionId,
                                  @NonNull final Long answerId,
                                  @NonNull final UpdateAnswerDto updateAnswer) {

        Question question = questionService.getByIdOrThrow(questionId);
        Answer answer = getIdOrThrow(answerId);
        answer.setAnswerText(updateAnswer.answerText());
        answer.setCorrect(updateAnswer.isCorrect());
        answer = answersRepository.save(answer);
        return AnswerDto.from(answer);
    }

    @Override
    public AnswerDto deleteAnswer(@NonNull final Long answerId) {

        Answer answer = getIdOrThrow(answerId);
        answersRepository.delete(answer);
        return AnswerDto.from(answer);
    }

    private Answer getIdOrThrow(Long answerId) {
        return answersRepository.findById(answerId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Answer with id <" + answerId + "> not found"));
    }
}