package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.exam.NewFinishExamDto;
import de.ait.smarttestit.models.Exam;

public interface FinishExamService {

    String finishExam(NewFinishExamDto newFinishExamDto);

    void updateExamAtFinish(NewFinishExamDto newFinishExamDto);
}
