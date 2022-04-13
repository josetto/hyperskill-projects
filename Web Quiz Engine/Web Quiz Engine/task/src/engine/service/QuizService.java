package engine.service;

import engine.model.Quiz;
import engine.model.QuizCompleted;
import engine.repository.QuizCompletedRepository;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizCompletedRepository quizCompletedRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, QuizCompletedRepository quizCompletedRepository) {
        this.quizRepository = quizRepository;
        this.quizCompletedRepository = quizCompletedRepository;
    }

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> findQuizById(long id) {
        return quizRepository.findById(id);
    }

    public Page findAll(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public boolean isAValidQuiz(Quiz quiz) {
        if(quiz.getOptions() == null) {
            return false;
        }
        if(quiz.getOptions().size() < 2) {
            return false;
        }
        if (quiz.getAnswers() == null) {
            quiz.setAnswer(new ArrayList<Integer>());
        }
        return true;
    }

    public void deleteQuiz(Quiz quiz) {
        List<QuizCompleted> quizCompletedList = quizCompletedRepository.findByQuizId(quiz.getId());
        quizCompletedList.forEach(q -> quizCompletedRepository.delete(q));
        quizRepository.delete(quiz);
    }
}