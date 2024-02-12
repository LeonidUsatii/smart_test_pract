package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.TestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTypesRepository extends JpaRepository<TestType, Long> {

    boolean existsByName(String name);
}
