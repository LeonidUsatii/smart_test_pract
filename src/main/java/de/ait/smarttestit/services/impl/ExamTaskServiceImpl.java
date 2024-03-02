package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.applicant.NewApplicantTaskDto;
import de.ait.smarttestit.dto.exam_task.ExamTaskDto;
import de.ait.smarttestit.dto.exam_task.UpdateExamTaskDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.ExamTaskRepository;
import de.ait.smarttestit.services.ExamTasksService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ExamTaskServiceImpl implements ExamTasksService {

    private final ExamTaskRepository examTaskRepository;
    private final QuestionServiceImpl questionService;

    @Override
    public boolean isExamTaskTitleUnique(String examTaskTitle) {
        return examTaskRepository.findByExamTaskTitle(examTaskTitle).isEmpty();
    }

    @Override
    public ExamTask examTaskForExam(NewApplicantTaskDto applicantTaskDto){

        List<TestType> testTypes = questionService.createTestTypesForExam(applicantTaskDto);
        return new ExamTask(applicantTaskDto.examTitle(),testTypes);
    }

    @Override
    public ExamTask save(ExamTask examTask) {
        Long examTaskId = examTask.getId();;
        if(examTaskId != null && examTaskRepository.existsById(examTask.getId()))  {
            throw new DataIntegrityViolationException("ExamTask with ID " + examTask.getId() + " already exists.");
        }
        else {
            return examTaskRepository.save(examTask);
        }
    }

    @Override
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
}