package de.ait.smarttestit.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 1000)
    private String answerText;

    @NotNull
    @Column(nullable = false)
    private boolean isCorrect;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Answer(String answerText, boolean isCorrect, Question question) {
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", answerText='" + answerText + '\'' +
                ", isCorrect=" + isCorrect +
                ", questionId=" + question.getId() +
                '}';
    }
}
