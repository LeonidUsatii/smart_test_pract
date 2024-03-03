package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam_task.ExamTaskWithTestTypeDto;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.ExamStatus;
import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.repositories.ExamRepository;
import de.ait.smarttestit.services.ExamService;
import de.ait.smarttestit.services.StartExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class StartExamServiceImpl implements StartExamService {

    private final ExamService examService;

    private final ExamRepository examRepository;

    @Override
    public ExamTaskWithTestTypeDto getExamTaskForExam(Long examId) {

        ExamTask examTask = complementsExamAtStartup(examId).getExamTask();

        return ExamTaskWithTestTypeDto.from(examTask, examId);
    }

    @Override
    public   Exam complementsExamAtStartup(Long examId) {

        Exam exam = examService.getExamOrThrow(examId);

        exam.setExamStartTime(LocalDateTime.now());
        exam.setExamStatus(ExamStatus.UNDERWAY);

        return examRepository.save(exam);
    }
}
