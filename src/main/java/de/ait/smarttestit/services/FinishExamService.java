package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.exam.NewFinishExamDto;

public interface FinishExamService {

    String finishExam(NewFinishExamDto newFinishExamDto);

    void updateExamAtFinish(NewFinishExamDto newFinishExamDto);
}
