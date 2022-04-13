package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Quiz {
    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @ElementCollection
    private List<String> options = new ArrayList<>();

    @ElementCollection
    @JsonIgnore
    private List<Integer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User author;

    public Quiz(long id, String title, String text, List<String> options, List<Integer> answers) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
        this.answers = answers;
    }

    public Quiz() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswer(List<Integer> answers) {
        this.answers = answers;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", options=" + options +
                ", answers=" + answers +
                ", author=" + author +
                '}';
    }
}