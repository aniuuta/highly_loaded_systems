package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.User;
import ru.hpclab.hl.module1.model.UserTDO;
import ru.hpclab.hl.module1.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Получить всех пользователей
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    // Получить пользователя по ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    // Удалить пользователя по ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }


    // Сохранить нового пользователя
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody UserTDO user) {
        // 1. Конвертируем DTO в Entity и сохраняем
        User userCreate = new User(
                user.getFio(),
                user.getLogin(),
                user.getEmail()
        );

        return ResponseEntity.ok(userService.saveUser(userCreate));
    }

    // Обновить пользователя по ID
    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // Пользовательские методы

    // Получить пользователя по логину
    @GetMapping("/login/{login}")
    public User getUserByLogin(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

    // Получить пользователя по email
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // Получить пользователей, зарегистрированных после определенной даты
    @GetMapping("/registered-after")
    public List<User> getUsersRegisteredAfter(@RequestParam LocalDateTime date) {
        return userService.getUsersRegisteredAfter(date);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllUsers() {
        userService.clearAll();
        return ResponseEntity.ok().build();
    }
}