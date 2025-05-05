package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.User;
import ru.hpclab.hl.module1.model.UserTDO;
import ru.hpclab.hl.module1.service.UserService;
import ru.hpclab.hl.module1.service.ObservabilityService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ObservabilityService observabilityService;

    @Autowired
    public UserController(UserService userService, ObservabilityService observabilityService) {
        this.userService = userService;
        this.observabilityService = observabilityService;
    }

    private <T> T measure(String metricName, java.util.function.Supplier<T> operation) {
        long start = System.currentTimeMillis();
        try {
            return operation.get();
        } finally {
            observabilityService.recordTiming(metricName, System.currentTimeMillis() - start);
        }
    }

    private void measure(String metricName, Runnable operation) {
        long start = System.currentTimeMillis();
        try {
            operation.run();
        } finally {
            observabilityService.recordTiming(metricName, System.currentTimeMillis() - start);
        }
    }

    @GetMapping
    public List<User> getUsers() {
        return measure("controller.user.get_all", userService::getAllUsers);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return measure("controller.user.get_by_id", () -> userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        measure("controller.user.delete", () -> userService.deleteUser(id));
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody UserTDO user) {
        return measure("controller.user.save", () -> {
            User userCreate = new User(
                    user.getFio(),
                    user.getLogin(),
                    user.getEmail()
            );
            return ResponseEntity.ok(userService.saveUser(userCreate));
        });
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable long id, @RequestBody User user) {
        return measure("controller.user.update", () -> userService.updateUser(id, user));
    }

    @GetMapping("/login/{login}")
    public User getUserByLogin(@PathVariable String login) {
        return measure("controller.user.get_by_login", () -> userService.getUserByLogin(login));
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return measure("controller.user.get_by_email", () -> userService.getUserByEmail(email));
    }

    @GetMapping("/registered-after")
    public List<User> getUsersRegisteredAfter(@RequestParam LocalDateTime date) {
        return measure("controller.user.get_registered_after", () -> userService.getUsersRegisteredAfter(date));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllUsers() {
        measure("controller.user.clear_all", userService::clearAll);
        return ResponseEntity.ok().build();
    }
}
