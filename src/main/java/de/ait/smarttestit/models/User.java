package de.ait.smarttestit.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String hashPassword;

    @NotNull
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;


    @PositiveOrZero
    @Column(nullable = false)
    private int levelOfUser;

    @Column(nullable = false)
    @OneToMany(mappedBy = "user")
    private List<Exam> exams = new ArrayList<>();

    public User(String firstName,
                String lastName,
                String email,
                String hashPassword,
                UserRole userRole,
                int levelOfUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashPassword = hashPassword;
        this.userRole = userRole;
        this.levelOfUser = levelOfUser;
    }

    public User(Long id, String firstName, String lastName, String email, String hashPassword, UserRole userRole, int levelOfUser) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashPassword = hashPassword;
        this.userRole = userRole;
        this.levelOfUser = levelOfUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", role=" + userRole +
                ", levelOfUser=" + levelOfUser +
                ", exams=" + exams +
                '}';
    }
}
