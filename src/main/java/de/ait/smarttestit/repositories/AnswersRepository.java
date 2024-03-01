package de.ait.smarttestit.repositories;

import de.ait.smarttestit.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswersRepository extends JpaRepository<Answer, Long> {

    boolean existsByAnswerTextAndQuestionId(String answerText, Long questionId);
}
