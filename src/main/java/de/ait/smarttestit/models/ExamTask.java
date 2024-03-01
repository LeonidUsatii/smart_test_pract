package de.ait.smarttestit.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExamTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 100)
    private String examTaskTitle;

    @OneToMany(mappedBy = "examTask", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TestType> testTypes = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    public ExamTask(String testTitle) {
        this.examTaskTitle = testTitle;
    }

    public ExamTask(Long id, String examTaskTitle) {
        this.id = id;
        this.examTaskTitle = examTaskTitle;
    }

    public ExamTask(String name, List<TestType> testTypes) {
        this.examTaskTitle = name;
        this.testTypes = testTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExamTask test = (ExamTask) o;

        return Objects.equals(id, test.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ExamTask{" +
                "id=" + id +
                ", examTaskTitle='" + examTaskTitle + '\'' +
                ", testTypes=" + testTypes +
                ", examId=" + (exam != null ? exam.getId() : null) +
                '}';
    }
}