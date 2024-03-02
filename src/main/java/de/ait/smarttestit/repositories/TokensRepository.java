package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokensRepository extends JpaRepository<Token, Long> {

   Optional<Token> findByCode(String tokenCode);
}
