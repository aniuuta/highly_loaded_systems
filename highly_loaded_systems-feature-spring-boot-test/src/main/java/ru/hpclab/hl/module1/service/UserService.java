package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.User;
import ru.hpclab.hl.module1.repository.InMemoryUserRepository;
import ru.hpclab.hl.module1.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        System.out.println("Repository injected: " + (userRepository != null));
        this.userRepository = userRepository;
    }

    // Получить всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Получить пользователя по ID
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    // Сохранить нового пользователя
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Удалить пользователя по ID
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    // Обновить пользователя по ID
    public User updateUser(UUID id, User user) {
        user.setIdentifier(id);
        return userRepository.save(user);
    }

    // Пользовательские методы на JPA

    // Получить пользователя по логину
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    // Получить пользователя по email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Получить пользователей, зарегистрированных после определенной даты
    public List<User> getUsersRegisteredAfter(LocalDateTime date) {
        return userRepository.findByDateRegistryAfter(date);
    }
}