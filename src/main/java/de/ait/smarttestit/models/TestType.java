package de.ait.smarttestit.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TestType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "testType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Question> questions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "test_id")
    private ExamTask test;

    public TestType(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestType testType = (TestType) o;
        return Objects.equals(id, testType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TestType{" +
                "id=" + id +
                ", name='" + name +
                ", questions=" + questions +
                ", testId=" + test.getId() +
                '}';
    }
}
