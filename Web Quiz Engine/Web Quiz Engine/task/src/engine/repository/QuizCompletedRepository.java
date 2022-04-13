package engine.repository;

import engine.model.QuizCompleted;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizCompletedRepository extends JpaRepository<QuizCompleted, Long> {

    Page findByUserId(long id, Pageable pageable);

    @Query("SELECT q from QuizCompleted q where q.user.id = ?1")
    Page<QuizCompleted> getUserIdAndDateByUserId(long id, Pageable pageable);

    @Query("SELECT q from QuizCompleted q WHERE q.quiz.id = ?1")
    List<QuizCompleted> findByQuizId(long id);
}
