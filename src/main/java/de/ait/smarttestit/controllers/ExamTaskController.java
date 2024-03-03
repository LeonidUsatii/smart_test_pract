package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.ExamTaskApi;
import de.ait.smarttestit.services.ExamTasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExamTaskController implements ExamTaskApi {

    private final ExamTasksService examTasksService;
    @Override
    public ResponseEntity<Void> deleteExamTask(Long examTaskId) {
        examTasksService.deleteExamTask(examTaskId);
        return ResponseEntity.noContent().build();
    }
}
