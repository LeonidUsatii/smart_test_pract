package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.FinishExamApi;
import de.ait.smarttestit.dto.exam.NewFinishExamDto;
import de.ait.smarttestit.services.FinishExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FinishExamController implements FinishExamApi {

    private final FinishExamService finishExamService;

    @Override
    public String finishExam(NewFinishExamDto newFinishExamDto) {

        return finishExamService.finishExam(newFinishExamDto);
    }
}
