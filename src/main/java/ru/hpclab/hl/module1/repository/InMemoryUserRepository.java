package ru.hpclab.hl.module1.repository;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository {
    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private final Map<String, UUID> loginToIdMap = new ConcurrentHashMap<>();
    private final Map<String, UUID> emailToIdMap = new ConcurrentHashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    public User save(User user) {

        // Проверка уникальности логина и email
        if (loginToIdMap.containsKey(user.getLogin())) {
            throw new IllegalStateException("Login already exists");
        }
        if (emailToIdMap.containsKey(user.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        users.put(user.getIdentifier(), user);
        loginToIdMap.put(user.getLogin(), user.getIdentifier());
        emailToIdMap.put(user.getEmail(), user.getIdentifier());
        return user;
    }

    public void deleteById(UUID id) {
        User user = users.get(id);
        if (user != null) {
            users.remove(id);
            loginToIdMap.remove(user.getLogin());
            emailToIdMap.remove(user.getEmail());
        }
    }

    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(loginToIdMap.get(login))
                .map(users::get);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(emailToIdMap.get(email))
                .map(users::get);
    }

    public List<User> findByDateRegistryAfter(LocalDateTime date) {
        return users.values().stream()
                .filter(user -> user.getDateRegistry().isAfter(date))
                .toList();
    }

    public User update(User user) {
        if (!users.containsKey(user.getIdentifier())) {
            throw new NoSuchElementException("User not found");
        }

        User existing = users.get(user.getIdentifier());

        // Обновляем логин, если он изменился
        if (!existing.getLogin().equals(user.getLogin())) {
            loginToIdMap.remove(existing.getLogin());
            loginToIdMap.put(user.getLogin(), user.getIdentifier());
        }

        // Обновляем email, если он изменился
        if (!existing.getEmail().equals(user.getEmail())) {
            emailToIdMap.remove(existing.getEmail());
            emailToIdMap.put(user.getEmail(), user.getIdentifier());
        }

        users.put(user.getIdentifier(), user);
        return user;
    }
}