package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByCodeAndExpiredDateTimeAfter(String tokenCode, LocalDateTime now);

    Optional<Token> findByCode(String tokenCode);
}