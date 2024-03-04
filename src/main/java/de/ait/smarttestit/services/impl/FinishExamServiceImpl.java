package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam.NewFinishExamDto;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.ExamStatus;
import de.ait.smarttestit.repositories.ExamRepository;
import de.ait.smarttestit.services.ExamService;
import de.ait.smarttestit.services.FinishExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class FinishExamServiceImpl implements FinishExamService {

    private final ExamService examService;

    private final ExamRepository examRepository;

    @Override
    public String finishExam(NewFinishExamDto newFinishExamDto) {

        updateExamAtFinish(newFinishExamDto);

        return "Thank you, the exam has been successfully completed.";
    }

    @Override
    public   void updateExamAtFinish(NewFinishExamDto newFinishExamDto) {

        Exam exam = examService.getExamOrThrow(newFinishExamDto.examId());

        int examScore = examService.getExamScore(newFinishExamDto);

        exam.setExamEndTime(LocalDateTime.now());
        exam.setExamStatus(ExamStatus.COMPLETED);
        exam.setExamScore(examScore);
        examRepository.save(exam);
    }
}
