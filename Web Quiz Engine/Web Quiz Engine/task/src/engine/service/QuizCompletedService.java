package engine.service;

import engine.model.QuizCompleted;
import engine.repository.QuizCompletedRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizCompletedService {


    private final QuizCompletedRepository quizCompletedRepository;

    public QuizCompletedService(QuizCompletedRepository quizCompletedRepository) {
        this.quizCompletedRepository = quizCompletedRepository;
    }
    Page getCompletedQuizzesByUserId(long id, Pageable pageable) {
        return quizCompletedRepository.findByUserId(id, pageable);
    }

    public QuizCompleted save(QuizCompleted quiz) {
        return quizCompletedRepository.save(quiz);
    }

    public Page<QuizCompleted> getUserIdAndDateByUserId(long id, Pageable pageable) {
        return quizCompletedRepository.getUserIdAndDateByUserId(id, pageable);
    }
    public List<QuizCompleted> findAll(){
        return quizCompletedRepository.findAll();
    }
}
