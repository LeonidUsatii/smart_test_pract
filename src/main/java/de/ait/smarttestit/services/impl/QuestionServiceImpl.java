package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.dto.exam_task.NewTestsParamDto;
import de.ait.smarttestit.dto.question.NewQuestionDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.dto.question.UpdateQuestionDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.QuestionRepository;
import de.ait.smarttestit.services.QuestionService;
import de.ait.smarttestit.services.TestTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final TestTypeService testTypeService;

    Random random = new Random();

    @Override
    @Transactional
    public QuestionDto addQuestion(@NonNull final Long testTypeId,
                                   @NonNull final NewQuestionDto newQuestion) {

        TestType testType = testTypeService.getByIdOrThrow(testTypeId);
        if (questionRepository.existsByQuestionTextAndTestTypeId(newQuestion.questionText(), testTypeId)) {
            throw new RestException(HttpStatus.CONFLICT,
                    "Question <" + newQuestion + "> already exists");
        }
        Question question = new Question(newQuestion.questionText(), newQuestion.level(), testType);
        Question savedQuestion = questionRepository.save(question);
        return QuestionDto.from(savedQuestion);
    }
    @Override
    public List<TestType> createTestTypesForExam(NewApplicantTaskDto applicantTaskDto){
        List<NewTestsParamDto> examTaskDtos = applicantTaskDto.examTaskDtoList();
        List<TestType> testTypes = new ArrayList<>();

        for(NewTestsParamDto testParam : examTaskDtos){
            TestType testType = createTestTypeForExam(testParam.getTestTypeId(),
                    testParam.getQuestionsLevel(), testParam.getQuestionsCount());
            testTypes.add(testType);
        }
        return testTypes;
    }

    @Override
    public TestType createTestTypeForExam(Long testTypeId,
                                          int questionLevel, int questionCount){

        return new TestType(
                getListQuestionsForTestType(testTypeId, questionLevel, questionCount));
    }

    @Override
    public List<QuestionDto> getAll() {
        return questionRepository.findAll().stream()
                .map(QuestionDto::from)
                .toList();
    }

    @Override
    public Question getByIdOrThrow(@NonNull final Long questionId) {
        return questionRepository.findById(questionId)
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
        question = questionRepository.save(question);
        return QuestionDto.from(question);
    }

    @Override
    public QuestionDto deleteQuestion(@NonNull final Long questionId) {

        Question question = getByIdOrThrow(questionId);
        questionRepository.delete(question);
        return QuestionDto.from(question);
    }

    @Override
    public List<Question> getListQuestionsForTestType(Long testTypeId,int questionLevel, int questionCount) {

        List<Question> filteredQuestions = new ArrayList<>();

        List<Question> suitableQuestions = questionRepository.findAllByTestTypeIdAndLevel(testTypeId, questionLevel);

        Set<Question> uniqueQuestions = new HashSet<>();

        while (uniqueQuestions.size() < questionCount && !suitableQuestions.isEmpty()) {
            int index = random.nextInt(suitableQuestions.size());
            Question question = suitableQuestions.get(index);

            if (uniqueQuestions.add(question)) {
                filteredQuestions.add(question);
            }
        }
        return filteredQuestions;
    }
}