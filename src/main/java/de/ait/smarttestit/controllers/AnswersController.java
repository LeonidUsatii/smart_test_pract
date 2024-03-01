package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.AnswersApi;
import de.ait.smarttestit.dto.answer.AnswerDto;
import de.ait.smarttestit.dto.answer.NewAnswerDto;
import de.ait.smarttestit.dto.answer.UpdateAnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import de.ait.smarttestit.services.AnswerService;

import java.util.List;

@RequiredArgsConstructor
@RestController
//@PreAuthorize("hasAnyAuthority('Teacher')")
public class AnswersController implements AnswersApi {

    private final AnswerService answersService;

    @Override
    public AnswerDto addAnswer(Long questionId, NewAnswerDto newAnswer) {
        return answersService.addAnswer(questionId, newAnswer);
    }

    @Override
    public List<AnswerDto> getAllAnswers() {
        return answersService.getAll();
    }

    @Override
    public AnswerDto updateAnswer(Long questionId, Long answerId, UpdateAnswerDto updateAnswer) {
        return answersService.updateAnswer(questionId, answerId, updateAnswer);
    }

    @Override
    public AnswerDto deleteAnswer(Long questionId, Long answerId) {
        return answersService.deleteAnswer(answerId);
    }
}
