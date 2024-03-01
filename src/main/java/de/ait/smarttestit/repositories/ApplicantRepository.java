package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    boolean existsByEmail(String email);
}
