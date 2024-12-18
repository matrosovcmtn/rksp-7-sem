package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = userRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestParam("username") String username,
                                           @RequestParam("password") String password) {
        User newUser = User.builder()
                .username(username)
                .password(password)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(newUser));
    }
}