package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean existsByQuestionTextAndTestTypeId(String s, Long testTypeId);

    List<Question> findAllByTestTypeIdAndLevel(long testTypeId, int level);
}