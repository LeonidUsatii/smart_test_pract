package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.exam_task.ExamTaskDto;
import de.ait.smarttestit.dto.exam_task.NewExamTaskDto;
import de.ait.smarttestit.dto.exam_task.UpdateExamTaskDto;
import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.models.TestType;
import lombok.NonNull;
import java.util.List;

public interface ExamTasksService {
    ExamTask getByIdOrThrow(@NonNull final Long testId);

    boolean isExamTaskTitleUnique(String examTaskTitle);

    ExamTaskDto addExamTask(@NonNull final NewExamTaskDto newExamTask);

    List<ExamTaskDto> getListExamTasks();

    ExamTaskDto deleteExamTask(@NonNull final Long examId);

    ExamTaskDto updateExamTask(@NonNull final Long examId, @NonNull final UpdateExamTaskDto updateExamTask);

    ExamTaskDto getExamTask(@NonNull final Long examId);

    List<TestType> addTestTypeToExamTask(@NonNull final Long examTaskId, @NonNull final NewTestTypeDto newTestType);

    List<TestType> addTestTypeToExamTask(@NonNull final Long examTaskId,  @NonNull final Long testTypeId);
}
