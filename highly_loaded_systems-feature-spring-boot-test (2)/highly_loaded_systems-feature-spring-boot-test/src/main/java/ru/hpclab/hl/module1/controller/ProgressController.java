package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.User;
import ru.hpclab.hl.module1.service.ProgressService;
import ru.hpclab.hl.module1.service.UserService;

import java.util.List;

@RestController
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return progressService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id) {
        return progressService.getUserById(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id) {
        progressService.deleteUser(id);
    }

    @PostMapping(value = "/users/")
    public User saveUser(@RequestBody User client) {
        return progressService.saveUser(client);
    }

    @PutMapping(value = "/users/{id}")
    public User updateUser(@PathVariable(required = false) String id, @RequestBody User user) {
        return progressService.Update(id, user);
    }

}
