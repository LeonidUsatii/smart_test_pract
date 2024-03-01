package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam_task.ExamTaskDto;
import de.ait.smarttestit.dto.exam_task.NewExamTaskDto;
import de.ait.smarttestit.dto.exam_task.UpdateExamTaskDto;
import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.ExamTaskRepository;
import de.ait.smarttestit.services.ExamTasksService;
import de.ait.smarttestit.services.TestTypeService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ExamTaskServiceImpl implements ExamTasksService {

    private final ExamTaskRepository examTaskRepository;
    private final TestTypeService testTypeService;

    @Override
    public boolean isExamTaskTitleUnique(String examTaskTitle) {
        return examTaskRepository.findByExamTaskTitle(examTaskTitle).isEmpty();
    }

    @Override
    public ExamTaskDto addExamTask(@NonNull NewExamTaskDto newExamTask) {

        if (!isExamTaskTitleUnique(newExamTask.examTaskTitle())) {
            throw new IllegalArgumentException("Exam task title must be unique");
        }

        ExamTask examTask = new ExamTask(newExamTask.examTaskTitle());
        ExamTask savedExamTask = examTaskRepository.save(examTask);

        return ExamTaskDto.from(savedExamTask);
    }

    @Override
    public ExamTaskDto getExamTask(@NonNull Long examId) {

        ExamTask examTask = getByIdOrThrow(examId);

        return ExamTaskDto.from(examTask);
    }

    public List<ExamTaskDto> getListExamTasks() {
        return examTaskRepository.findAll().stream()
                .map(ExamTaskDto::from)
                .toList();
    }

    @Override
    public ExamTaskDto deleteExamTask(@NonNull Long examId) {
        ExamTask examTask = getByIdOrThrow(examId);
        examTaskRepository.delete(examTask);
        return ExamTaskDto.from(examTask);
    }

    @Override
    public ExamTaskDto updateExamTask(@NonNull Long examId, @NonNull UpdateExamTaskDto updateExamTask) {

        ExamTask examTask = getByIdOrThrow(examId);

        examTask.setExamTaskTitle(updateExamTask.examTaskTitle());

        ExamTask updatedExamTask = examTaskRepository.save(examTask);

        return ExamTaskDto.from(updatedExamTask);
    }

    @Override
    public ExamTask getByIdOrThrow(@NonNull final Long testId) {
        return examTaskRepository.findById(testId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "ExamTask with id <" + testId + "> not found"));
    }

    @Override
    public List<TestType> addTestTypeToExamTask(@NonNull Long examTaskId, @NonNull NewTestTypeDto newTestType) {

        ExamTask examTask = getByIdOrThrow(examTaskId);

        TestTypeDto testTypeDto = testTypeService.addTestType(newTestType);

        TestType addedTestType = testTypeService.getByIdOrThrow(testTypeDto.id());

        List<TestType> updatedTestTypes = examTask.getTestTypes();

        updatedTestTypes.add(addedTestType);

        examTask.setTestTypes(updatedTestTypes);

        examTaskRepository.save(examTask);

        return updatedTestTypes;
    }

    @Override
    public List<TestType> addTestTypeToExamTask(@NonNull Long examTaskId, @NonNull Long testTypeId) {

        ExamTask examTask = getByIdOrThrow(examTaskId);

        TestType addedTestType = testTypeService.getByIdOrThrow(testTypeId);

        List<TestType> updatedTestTypes = examTask.getTestTypes();

        updatedTestTypes.add(addedTestType);

        examTask.setTestTypes(updatedTestTypes);

        examTaskRepository.save(examTask);

        return updatedTestTypes;
    }
}

