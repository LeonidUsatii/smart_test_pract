package de.ait.smarttestit.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestType> testTypes = new HashSet<>();

    @OneToOne(mappedBy = "examTask")
    @JoinColumn(name = "exam_id")
    private Exam exam;

    public ExamTask(String testTitle) {
        this.examTaskTitle = testTitle;
    }

    public ExamTask(Long id, String examTaskTitle) {
        this.id = id;
        this.examTaskTitle = examTaskTitle;
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
        return "Test{" +
                "id=" + id +
                ", testTitle='" + examTaskTitle + '\'' +
                ", testTypes=" + testTypes +
                '}';
    }
}
