package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hpclab.hl.module1.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    // Пользовательский запрос для поиска пользователя по логину
    @Query("SELECT u FROM User u WHERE u.login = :login")
    User findByLogin(@Param("login") String login);

    // Пользовательский запрос для поиска пользователя по email
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    // Пользовательский запрос для поиска пользователей, зарегистрированных после определенной даты
    @Query("SELECT u FROM User u WHERE u.dateRegistry > :date")
    List<User> findByDateRegistryAfter(@Param("date") LocalDateTime date);
}