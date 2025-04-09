package ru.hpclab.hl.module1.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @NonNull
    private UUID identifier;

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
        this.identifier = UUID.randomUUID();
        this.fio = fio;
        this.login = login;
        this.email = email;
        this.dateRegistry = LocalDateTime.now();
    }

    public User() {
    }
}