package de.ait.smarttestit.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EXAM")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    @Column
    private int examScore;

    private LocalDateTime examStartTime;

    private LocalDateTime examEndTime;

    @Min(5)
    @Max(180)
    @Column(nullable = false)
    private int examDuration;

    @NotNull
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ExamStatus examStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    @JsonBackReference
    private Applicant applicant;

    @OneToOne
    @JoinColumn(name = "examtask_id")
    private ExamTask examTask;

    /**
     * Class representing an Exam.<br>
     * Default value for examStatus is <b>ExamStatus.PLANNED</b>
     *
     * @param examScore     The score achieved in the exam.
     * @param examStartTime The start time of the exam.
     * @param examDuration  The duration of the exam in minutes.
     * @param user          The User taking the exam.
     */
    public Exam(int examScore,
                LocalDateTime examStartTime,
                LocalDateTime examEndTime,
                int examDuration,
                ExamStatus examStatus,
                User user,
                Applicant applicant,
                ExamTask examTask) {
        this.examScore = examScore;
        this.examStartTime = examStartTime;
        this.examEndTime = examEndTime;
        this.examDuration = examDuration;
        this.examStatus = ExamStatus.PLANNED;
        this.user = user;
        this.applicant = applicant;
        this.examTask = examTask;
    }

    public Exam(int examDuration, ExamStatus examStatus, Applicant applicant, ExamTask examTask) {
        this.examDuration = examDuration;
        this.examStatus = examStatus;
        this.applicant = applicant;
        this.examTask = examTask;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return Objects.equals(id, exam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", testScore=" + examScore +
                ", testStartTime=" + examStartTime +
                ", testEndTime=" + examEndTime +
                ", testDuration=" + examDuration +
                ", examStatus=" + examStatus.name() +
                ", userId=" + user.getId() +
                ", testId=" + examTask.getId() +
                '}';
    }
}
