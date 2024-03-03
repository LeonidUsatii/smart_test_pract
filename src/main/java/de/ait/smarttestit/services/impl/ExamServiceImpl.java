package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam.ExamDto;
import de.ait.smarttestit.dto.exam.NewExamDto;
import de.ait.smarttestit.dto.exam.UpdateExamDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.*;
import de.ait.smarttestit.repositories.ExamRepository;
import de.ait.smarttestit.services.ExamTasksService;
import de.ait.smarttestit.services.ExamService;
import de.ait.smarttestit.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static de.ait.smarttestit.dto.exam.ExamDto.from;

@RequiredArgsConstructor
@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final UserService userService;
    private final ExamTasksService examTasksService;

    @Override
    public ExamDto addExam(@NonNull final NewExamDto newExam) {

        Optional<ExamStatus> examStatus = ExamStatus.findByName(newExam.examStatus());

        if (examStatus.isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND,
                    "ExamStatus with name <" + newExam.examStatus() + "> not found");
        }

        ExamTask examTask = examTasksService.getByIdOrThrow(newExam.examTaskId());

        if (newExam.userId() == null)  {

            throw new RestException(HttpStatus.BAD_REQUEST, "Neither userId nor applicantId provided");
        }

        Exam exam = new Exam(
                newExam.examScore(),
                newExam.examStartTime(),
                newExam.examEndTime(),
                newExam.examDuration(),
                ExamStatus.valueOf(newExam.examStatus()),
                userService.getUserOrThrow(newExam.userId()),
                null,
                examTask
        );

        Exam savedExam = examRepository.save(exam);

        userService.addExamToUser(newExam.userId(), savedExam);

        return from(savedExam);
    }

    @Override
    public ExamDto getExam(@NonNull final Long examId) {

        Exam exam = getExamOrThrow(examId);
        return from(exam);
    }

    @Override
    public List<ExamDto> getListExams() {
        return examRepository.findAll().stream()
                .map(ExamDto::from)
                .toList();
    }

    @Override
    public ExamDto deleteExam(@NonNull final Long examId) {

        Exam exam = getExamOrThrow(examId);
        examRepository.delete(exam);
        return from(exam);
    }

    @Override
    public ExamDto updateExam(@NonNull final Long examId, @NonNull final UpdateExamDto updateExam) {

        Exam exam = getExamOrThrow(examId);
        User user = userService.getUserOrThrow(updateExam.userId());
        ExamTask examTask = examTasksService.getByIdOrThrow(updateExam.examTaskId());

        exam.setExamScore(updateExam.examScore());
        exam.setExamStartTime(updateExam.examStartTime());
        exam.setExamEndTime(updateExam.examEndTime());
        exam.setExamDuration(updateExam.examDuration());
        exam.setExamStatus(ExamStatus.valueOf(updateExam.examStatus()));
        exam.setUser(user);
        exam.setExamTask(examTask);

        exam = examRepository.save(exam);

        return from(exam);
    }

    @Override
    public Exam getExamOrThrow(@NonNull final Long examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Exam with id <" + examId + "> not found"));
    }

    @Override
    public Exam save(Exam exam) {
        Long examId = exam.getId();
        if(examId != null && examRepository.existsById(exam.getId()))  {
             throw new DataIntegrityViolationException("Exam with ID " + exam.getId() + " already exists.");
         }
         else {
            return examRepository.save(exam);
         }
    }
}