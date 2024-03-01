package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.QuestionsApi;
import de.ait.smarttestit.dto.question.NewQuestionDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.dto.question.UpdateQuestionDto;
import de.ait.smarttestit.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
//@PreAuthorize("hasAnyAuthority('Teacher')")
public class QuestionsController implements QuestionsApi {
    private final QuestionService questionService;


    @Override
    public QuestionDto addQuestion(Long testTypeId, NewQuestionDto newQuestion) {
        return questionService.addQuestion(testTypeId, newQuestion);
    }

    @Override
    public List<QuestionDto> getAllQuestions() {
        return questionService.getAll();
    }

    @Override
    public QuestionDto updateQuestion(Long testTypeId, Long questionId, UpdateQuestionDto updateQuestion) {
        return questionService.updateQuestion(testTypeId, questionId, updateQuestion);
    }

    @Override
    public QuestionDto deleteQuestion(Long questionId) {
        return questionService.deleteQuestion(questionId);
    }
}