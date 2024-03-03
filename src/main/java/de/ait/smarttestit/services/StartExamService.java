package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.exam_task.ExamTaskWithTestTypeDto;
import de.ait.smarttestit.models.Exam;

public interface StartExamService {

    ExamTaskWithTestTypeDto getExamTaskForExam(Long examId);

    Exam complementsExamAtStartup(Long examId);
}
