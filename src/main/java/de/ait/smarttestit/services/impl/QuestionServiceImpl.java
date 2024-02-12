package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.question.NewQuestionDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.dto.question.UpdateQuestionDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.QuestionsRepository;
import de.ait.smarttestit.services.QuestionService;
import de.ait.smarttestit.services.TestTypeService;
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
public class QuestionServiceImpl implements QuestionService {

    private final QuestionsRepository questionsRepository;
    private final TestTypeService testTypeService;

    @Override
    @Transactional
    public QuestionDto addQuestion(@NonNull final Long testTypeId,
                                   @NonNull final NewQuestionDto newQuestion) {

        TestType testType = getTestTypeOrThrow(testTypeId);
        if (questionsRepository.existsByQuestionTextAndTestTypeId(newQuestion.questionText(), testTypeId)) {
            throw new RestException(HttpStatus.CONFLICT,
                    "Question <" + newQuestion + "> already exists");
        }
        Question question = new Question();
                question.setQuestionText(newQuestion.questionText());
                question.setLevel(newQuestion.level());
                question.setTestType(testType);

        var savedQuestion = questionsRepository.save(question);
        return QuestionDto.from(savedQuestion);
    }

    @Override
    public List<QuestionDto> getAll() {
        return questionsRepository.findAll().stream()
                .map(QuestionDto::from)
                .collect(Collectors.toList());
    }
    @Override
    public TestType getTestTypeOrThrow(@NonNull final Long testTypeId) {
        return testTypeService.getByIdOrThrow(testTypeId);
    }
    @Override
    public Question getByIdOrThrow(@NonNull final Long questionId) {
        return questionsRepository.findById(questionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Question with id <" + questionId + "> not found"));
    }

    @Override
    public QuestionDto updateQuestion(@NonNull final Long testTypeId,
                                      @NonNull final Long questionId,
                                      @NonNull final UpdateQuestionDto updateQuestion) {

        TestType testType = testTypeService.getByIdOrThrow(updateQuestion.testTypeId());
        Question question = getByIdOrThrow(questionId);
        question.setQuestionText(updateQuestion.questionText());
        question.setLevel(updateQuestion.level());
        question.setTestType(testType);
        question = questionsRepository.save(question);
        return QuestionDto.from(question);
    }

    @Override
    public QuestionDto deleteQuestion(@NonNull final Long questionId) {

        Question question = getByIdOrThrow(questionId);
        questionsRepository.delete(question);
        return QuestionDto.from(question);
    }
}