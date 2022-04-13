package engine.service;

import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import engine.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

public class UserService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).get();
        if (user == null){
            throw new UsernameNotFoundException(email + " not found!");
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails
                .User(user.getEmail(), user.getPassword(), List.of(() -> "READ"));
        return userDetails;
    }

    public User save(User user) {

        Optional<User> optUser = userRepository.findByEmail(user.getEmail());
        if(optUser.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
