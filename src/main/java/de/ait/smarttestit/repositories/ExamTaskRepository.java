package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.ExamTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamTaskRepository extends JpaRepository<ExamTask, Long> {

    Optional<ExamTask> findByExamTaskTitle(String examTaskTitle);
}
