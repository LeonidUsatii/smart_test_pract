package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.ExamApi;
import de.ait.smarttestit.dto.exam.ExamDto;
import de.ait.smarttestit.dto.exam.NewExamDto;
import de.ait.smarttestit.dto.exam.UpdateExamDto;
import de.ait.smarttestit.services.ExamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExamsController implements ExamApi {

    private final ExamsService examsService;

    @Override
    public ExamDto addExam(int examScore, LocalDateTime examStartTime, LocalDateTime examEndTime, int examDuration,
                           String examStatus, Long userId, Long applicantId, Long examTaskId) {

        NewExamDto newExam = new NewExamDto(examScore, examStartTime, examEndTime, examDuration,
        examStatus, userId, applicantId, examTaskId);

        return examsService.addExam(newExam);
    }

    @Override
    public List<ExamDto> getListExams() {

        return examsService.getListExams();
    }

    @Override
    public ExamDto getExam(Long examId) {

        return examsService.getExam(examId);
    }

    @Override
    public ResponseEntity<Void> deleteExam(Long examId) {

        examsService.deleteExam(examId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ExamDto> updateExam(Long examId, int newExamScore, LocalDateTime newExamStartTime, LocalDateTime newExamEndTime, int newExamDuration,
                                              String newExamStatus, Long newUserId, Long newApplicantId, Long newExamTaskId) {

        UpdateExamDto updateExam = new UpdateExamDto(newExamScore, newExamStartTime, newExamEndTime, newExamDuration,
        newExamStatus, newUserId, newApplicantId, newExamTaskId);

        ExamDto updatedExam = examsService.updateExam(examId, updateExam);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedExam);
    }
}
