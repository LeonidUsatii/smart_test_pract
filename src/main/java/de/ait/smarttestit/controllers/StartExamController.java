package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.StartExamApi;
import de.ait.smarttestit.dto.exam_task.ExamTaskWithTestTypeDto;
import de.ait.smarttestit.services.StartExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StartExamController implements StartExamApi {

    private final StartExamService startExamService;

    @Override
    public ExamTaskWithTestTypeDto getExamTaskForExam(Long examId) {

        return startExamService.getExamTaskForExam(examId);
    }
}
