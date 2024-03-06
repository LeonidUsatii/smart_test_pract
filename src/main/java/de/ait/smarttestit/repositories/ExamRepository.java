package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.Exam;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface ExamRepository extends JpaRepository<Exam, Long> {

}
