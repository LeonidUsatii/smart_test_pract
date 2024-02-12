package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam.ExamDto;
import de.ait.smarttestit.dto.exam.NewExamDto;
import de.ait.smarttestit.dto.exam.UpdateExamDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.*;
import de.ait.smarttestit.repositories.ExamsRepository;
import de.ait.smarttestit.services.ApplicantsService;
import de.ait.smarttestit.services.ExamsService;
import de.ait.smarttestit.services.ExamTasksService;
import de.ait.smarttestit.services.UsersService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static de.ait.smarttestit.dto.exam.ExamDto.from;

@RequiredArgsConstructor
@Service
@Component
public class ExamsServiceImpl implements ExamsService {

    private final ExamsRepository examsRepository;
    private final UsersService usersService;
    private final ApplicantsService applicantsService;
    private final ExamTasksService examTasksService;

    @Override
    @Transactional
    public ExamDto addExam(@NonNull final NewExamDto newExam) {

        Optional<ExamStatus> examStatus = ExamStatus.findByName(newExam.examStatus());

        if(examStatus.isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND,
                    "ExamStatus with name <" + newExam.examStatus() + "> not found");
        }

        ExamTask examTask = examTasksService.getByIdOrThrow(newExam.examTaskId());

        if((newExam.userId() != null) && (newExam.applicantId() != null)) {

            throw new RestException(HttpStatus.BAD_REQUEST, "Both user and applicant are defined");
        }

        if((newExam.userId() == null) && (newExam.applicantId() == null)) {

            throw new RestException(HttpStatus.BAD_REQUEST, "Neither userId nor applicantId provided");
        }

        if(newExam.userId() != null) {

            Exam exam = new Exam(
                    newExam.examScore(),
                    newExam.examStartTime(),
                    newExam.examEndTime(),
                    newExam.examDuration(),
                    ExamStatus.valueOf(newExam.examStatus()),
                    usersService.getUserOrThrow(newExam.userId()),
                    null,
                    examTask
            );

            Exam savedExam = examsRepository.save(exam);

            usersService.addExamToUser(newExam.userId(), savedExam);

            return from(savedExam);

        } else  {

            Exam exam = new Exam(newExam.examScore(),
                    newExam.examStartTime(),
                    newExam.examEndTime(),
                    newExam.examDuration(),
                    ExamStatus.valueOf(newExam.examStatus()),
                    null,
                    applicantsService.getApplicantOrThrow(newExam.applicantId()),
                    examTask);

            Exam savedExam = examsRepository.save(exam);

            applicantsService.addExamToApplicant(newExam.applicantId(), savedExam);

            return from(savedExam);
        }
    }

    @Override
    public ExamDto getExam(@NonNull final Long examId) {

        Exam exam = getExamOrThrow(examId);
        return from(exam);
    }

    @Override
    public List<ExamDto> getListExams() {

        List<Exam> exams = examsRepository.findAll();

            return exams.stream()
                    .map(ExamDto::from)
                    .collect(Collectors.toList());
    }

    @Override
    public ExamDto deleteExam(@NonNull final Long examId) {

        Exam exam = getExamOrThrow(examId);
        examsRepository.delete(exam);
        return from(exam);
    }

    @Override
    public ExamDto updateExam(@NonNull final Long examId, @NonNull final UpdateExamDto updateExam) {

        Exam exam = getExamOrThrow(examId);
        User user = usersService.getUserOrThrow(updateExam.userId());
        ExamTask examTask = examTasksService.getByIdOrThrow(updateExam.examTaskId());

        exam.setExamScore(updateExam.examScore());
        exam.setExamStartTime(updateExam.examStartTime());
        exam.setExamEndTime(updateExam.examEndTime());
        exam.setExamDuration(updateExam.examDuration());
        exam.setExamStatus(ExamStatus.valueOf(updateExam.examStatus()));
        exam.setUser(user);
        exam.setExamTask(examTask);

        exam = examsRepository.save(exam);

        return from(exam);
    }
    @Override
    public Exam getExamOrThrow(@NonNull final Long examId) {
        return examsRepository.findById(examId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Exam with id <" + examId + "> not found"));
    }
}
