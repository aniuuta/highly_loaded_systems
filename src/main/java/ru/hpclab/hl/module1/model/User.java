package ru.hpclab.hl.module1.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio", nullable = false)
    @NonNull
    private String fio;

    @Column(name = "login", nullable = false, unique = true)
    @NonNull
    private String login;

    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    private String email;

    @Column(name = "date_registry", nullable = false)
    @NonNull
    private LocalDateTime dateRegistry;

    public User(@NonNull String fio, @NonNull String login, @NonNull String email) {
        this.fio = fio;
        this.login = login;
        this.email = email;
        this.dateRegistry = LocalDateTime.now();
    }

    public User() {
        // Нужен пустой конструктор для JPA
    }
}
