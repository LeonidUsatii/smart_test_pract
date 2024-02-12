package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam_task.ExamTaskDto;
import de.ait.smarttestit.dto.exam_task.NewExamTaskDto;
import de.ait.smarttestit.dto.exam_task.UpdateExamTaskDto;
import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.*;
import de.ait.smarttestit.repositories.ExamTasksRepository;
import de.ait.smarttestit.services.ExamTasksService;
import de.ait.smarttestit.services.QuestionService;
import de.ait.smarttestit.services.TestTypeService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static de.ait.smarttestit.dto.exam_task.ExamTaskDto.from;

@AllArgsConstructor
@Service
@Component
public class ExamTasksServiceImpl implements ExamTasksService {

    private final ExamTasksRepository examTasksRepository;

    private final QuestionService questionService;

    private final TestTypeService testTypeService;

    @Override
    public boolean isExamTaskTitleUnique(String examTaskTitle) {
        return examTasksRepository.findByExamTaskTitle(examTaskTitle).isEmpty();
    }

    @Override
    public ExamTaskDto addExamTask(@NonNull NewExamTaskDto newExamTask) {

        if(!isExamTaskTitleUnique(newExamTask.examTaskTitle())) {
            throw new IllegalArgumentException("Exam task title must be unique");
        }

        ExamTask examTask = new ExamTask();

        examTask.setExamTaskTitle(newExamTask.examTaskTitle());

        ExamTask savedExamTask = examTasksRepository.save(examTask);

        return from(savedExamTask);
    }

    @Override
    public ExamTaskDto getExamTask(@NonNull Long examId) {

        ExamTask examTask = getByIdOrThrow(examId);

        return ExamTaskDto.from(examTask);
    }

    public List<ExamTaskDto> getListExamTasks() {
        List<ExamTask> examTasks = examTasksRepository.findAll();
        return examTasks.stream()
                .map(ExamTaskDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public ExamTaskDto deleteExamTask(@NonNull Long examId) {
        ExamTask examTask = getByIdOrThrow(examId);
        examTasksRepository.delete(examTask);
        return ExamTaskDto.from(examTask);
    }

    @Override
    public ExamTaskDto updateExamTask(@NonNull Long examId, @NonNull UpdateExamTaskDto updateExamTask) {

        ExamTask examTask = getByIdOrThrow(examId);

        examTask.setExamTaskTitle(updateExamTask.examTaskTitle());

        ExamTask updatedExamTask = examTasksRepository.save(examTask);

        return ExamTaskDto.from(updatedExamTask);
    }

    @Override
    public ExamTask getByIdOrThrow(@NonNull final Long testId) {
        return examTasksRepository.findById(testId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "ExamTask with id <" + testId + "> not found"));
    }

    @Override
    public Set<TestType> addTestTypeToExamTask(@NonNull Long examTaskId, @NonNull NewTestTypeDto newTestType) {

        ExamTask examTask = getByIdOrThrow(examTaskId);

        TestTypeDto testTypeDto = testTypeService.addTestType(newTestType);

        TestType addedTestType = testTypeService.getByIdOrThrow(testTypeDto.id());

        Set<TestType> updatedTestTypes = examTask.getTestTypes();

        updatedTestTypes.add(addedTestType);

        examTask.setTestTypes(updatedTestTypes);

        examTasksRepository.save(examTask);

        return updatedTestTypes;
    }

    @Override
    public Set<TestType> addTestTypeToExamTask(@NonNull Long examTaskId, @NonNull Long testTypeId) {

        ExamTask examTask = getByIdOrThrow(examTaskId);

        TestType addedTestType = testTypeService.getByIdOrThrow(testTypeId);

        Set<TestType> updatedTestTypes = examTask.getTestTypes();

        updatedTestTypes.add(addedTestType);

        examTask.setTestTypes(updatedTestTypes);

        examTasksRepository.save(examTask);

        return updatedTestTypes;
    }
}

