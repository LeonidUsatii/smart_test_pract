package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.exam.ExamDto;
import de.ait.smarttestit.dto.exam.NewExamDto;
import de.ait.smarttestit.dto.exam.UpdateExamDto;
import de.ait.smarttestit.models.Exam;
import lombok.NonNull;

import java.util.List;

public interface ExamService {

    Exam getExamOrThrow(@NonNull final Long examId);

    ExamDto addExam(@NonNull final NewExamDto newExamDto);

    List<ExamDto> getListExams();

    ExamDto deleteExam(@NonNull final Long examId);

    ExamDto updateExam(@NonNull final Long examId, @NonNull final UpdateExamDto updateExam);

    ExamDto getExam(@NonNull final Long examId);

    Exam save(Exam exam);
}
