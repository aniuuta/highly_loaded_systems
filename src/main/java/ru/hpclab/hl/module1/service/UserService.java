package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.User;
import ru.hpclab.hl.module1.repository.UserRepository;
import ru.hpclab.hl.module1.service.ObservabilityService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ObservabilityService observabilityService;

    @Autowired
    public UserService(UserRepository userRepository, ObservabilityService observabilityService) {
        this.userRepository = userRepository;
        this.observabilityService = observabilityService;
    }

    public List<User> getAllUsers() {
        long start = System.currentTimeMillis();
        try {
            return userRepository.findAll();
        } finally {
            observabilityService.recordTiming("service.user.get_all", System.currentTimeMillis() - start);
        }
    }

    public User getUserById(long id) {
        long start = System.currentTimeMillis();
        try {
            return userRepository.findById(id).orElse(null);
        } finally {
            observabilityService.recordTiming("service.user.get_by_id", System.currentTimeMillis() - start);
        }
    }

    public User saveUser(User user) {
        long start = System.currentTimeMillis();
        try {
            return userRepository.save(user);
        } finally {
            observabilityService.recordTiming("service.user.save", System.currentTimeMillis() - start);
        }
    }

    public void deleteUser(long id) {
        long start = System.currentTimeMillis();
        try {
            userRepository.deleteById(id);
        } finally {
            observabilityService.recordTiming("service.user.delete", System.currentTimeMillis() - start);
        }
    }

    public User updateUser(long id, User user) {
        long start = System.currentTimeMillis();
        try {
            user.setId(id);
            return userRepository.save(user);
        } finally {
            observabilityService.recordTiming("service.user.update", System.currentTimeMillis() - start);
        }
    }

    public User getUserByLogin(String login) {
        long start = System.currentTimeMillis();
        try {
            return userRepository.findByLogin(login);
        } finally {
            observabilityService.recordTiming("service.user.get_by_login", System.currentTimeMillis() - start);
        }
    }

    public User getUserByEmail(String email) {
        long start = System.currentTimeMillis();
        try {
            return userRepository.findByEmail(email);
        } finally {
            observabilityService.recordTiming("service.user.get_by_email", System.currentTimeMillis() - start);
        }
    }

    public List<User> getUsersRegisteredAfter(LocalDateTime date) {
        long start = System.currentTimeMillis();
        try {
            return userRepository.findByDateRegistryAfter(date);
        } finally {
            observabilityService.recordTiming("service.user.get_registered_after", System.currentTimeMillis() - start);
        }
    }

    public void clearAll() {
        long start = System.currentTimeMillis();
        try {
            userRepository.deleteAll();
        } finally {
            observabilityService.recordTiming("service.user.clear_all", System.currentTimeMillis() - start);
        }
    }
}
