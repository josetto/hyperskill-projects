package engine.controller;

import engine.model.Quiz;
import engine.model.QuizCompleted;
import engine.model.User;
import engine.service.QuizCompletedService;
import engine.service.QuizService;
import engine.service.UserService;
import engine.util.ClientAnswer;
import engine.util.QuizResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/quizzes")
@Validated
public class QuizController {

    private final QuizService quizService;
    private final QuizResponse quizResponse;
    private final QuizCompletedService quizCompletedService;
    private final UserService userService;

    private final static Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    public QuizController(QuizService quizService, QuizResponse quizResponse, UserService userService, QuizCompletedService quizCompletedService) {
        this.quizService = quizService;
        this.quizResponse = quizResponse;
        this.quizCompletedService = quizCompletedService;
        this.userService = userService;
    }

    @PostMapping
    public Quiz createQuiz(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Quiz quiz) {
        User user = userService.findByEmail(userDetails.getUsername()).get();
        quiz.setAuthor(user);
        boolean isValid = quizService.isAValidQuiz(quiz);
        if (!isValid){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        logger.info(user + " has created: " + quiz);
        return quizService.saveQuiz(quiz);
    }

    @GetMapping
    public Page getAllQuizzes(@RequestParam(defaultValue = "0", required = false) int page) {
        Pageable paging = PageRequest.of(page, 10);
        return quizService.findAll(paging);
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable long id) {
        Optional<Quiz> optionalQuiz = quizService.findQuizById(id);
        if(optionalQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalQuiz.get();
    }

    @PostMapping("/{id}/solve")
    public QuizResponse solveAQuiz(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable long id,
                                   @NotNull @RequestBody ClientAnswer clientAnswer) {
        Optional<Quiz> optionalQuiz = quizService.findQuizById(id);
        if (optionalQuiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Quiz quiz = optionalQuiz.get();
        List<Integer> clientAnswerList = clientAnswer.getAnswer();
        List<Integer> quizAnswersList = quiz.getAnswers();

        boolean success = clientAnswerList.equals(quizAnswersList);
        quizResponse.setSuccess(success);

        logger.info("client answer: " + clientAnswerList);
        logger.info("quiz answer: " + quizAnswersList);
        logger.info("result: " + success);
        if(!success) {
            return quizResponse;
        }

        User user = userService.findByEmail(userDetails.getUsername()).get();

        QuizCompleted quizCompleted = new QuizCompleted();
        quizCompleted.setCompletedAt(LocalDateTime.now());
        quizCompleted.setUser(user);
        quizCompleted.setQuiz(quiz);
        quizCompletedService.save(quizCompleted);
        logger.info(user + " has solved: " + quiz + "\nresult: " + quizCompleted);

        return quizResponse;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuiz(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Quiz> optionalQuiz = quizService.findQuizById(id);
        Quiz quiz = optionalQuiz.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User currentUser = userService.findByEmail(userDetails.getUsername()).get();
        User author = quiz.getAuthor();

        if(!currentUser.equals(author)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        logger.info(currentUser.getId() + "is deleting: " + quiz);

        quizService.deleteQuiz(quiz);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/completed")
    public ResponseEntity getCompletedQuizzes(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam(defaultValue = "0", required = false) int page) {
        User user = userService.findByEmail(userDetails.getUsername()).get();
        Pageable paging = PageRequest.of(page,10, Sort.by("completedAt").descending());

        logger.info("User soliciting its completed quizzes: " + user.getId());
        Page pageData = quizCompletedService.getUserIdAndDateByUserId(user.getId(), paging);

        List<Map> list = new ArrayList<>();
        pageData.forEach(quiz -> {
            System.out.println(quiz);
            Map<String, Object> mapQuiz = new HashMap<>();
            mapQuiz.put("id", ((QuizCompleted)quiz).getQuiz().getId());
            mapQuiz.put("completedAt", ((QuizCompleted) quiz).getCompletedAt());
            list.add(mapQuiz);
        });

        Map<String, Object> mapResponse = new HashMap<>();
        mapResponse.put("totalPages", pageData.getTotalPages());
        mapResponse.put("totalElements", pageData.getTotalElements());
        mapResponse.put("last", pageData.isLast());
        mapResponse.put("first", pageData.isFirst());
        mapResponse.put("empty", pageData.isEmpty());
        mapResponse.put("content", list);
        return new ResponseEntity(mapResponse, HttpStatus.OK);
    }
}