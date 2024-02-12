package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamsRepository extends JpaRepository<Exam, Long> {

}
