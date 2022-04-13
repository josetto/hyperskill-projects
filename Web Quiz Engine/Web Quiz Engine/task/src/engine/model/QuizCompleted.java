package engine.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class QuizCompleted {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn
    private User user;
    @ManyToOne
    @JoinColumn
    private Quiz quiz;
    private LocalDateTime completedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "QuizCompleted{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", quiz=" + quiz.getId() +
                ", completedAt=" + completedAt +
                '}';
    }
}
