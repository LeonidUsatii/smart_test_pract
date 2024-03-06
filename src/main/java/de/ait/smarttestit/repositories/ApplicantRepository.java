package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.Applicant;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Hidden
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    boolean existsByEmail(String email);

    Optional<Applicant> findFirstByTokensCode(String tokenCode);
}