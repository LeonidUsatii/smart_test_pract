package de.ait.smarttestit.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 1000)
    @Column(nullable = false, length = 1000)
    private String questionText;

    @NotNull
    @Min(1)
    @Max(10)
    @Column(nullable = false)
    private Integer level;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "testType_id", nullable = false)
    @JsonBackReference
    private TestType testType;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Answer> answers = new ArrayList<>();

    public Question(String questionText, Integer level, TestType testType) {
        this.questionText = questionText;
        this.level = level;
        this.testType = testType;
    }

    public Question(Long id, String questionText, List<Answer> answers) {
        this.id = id;
        this.questionText=questionText;
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", level=" + level +
                ", testTypeId=" + testType.getId() +
                ", answers=" + answers +
                '}';
    }
}