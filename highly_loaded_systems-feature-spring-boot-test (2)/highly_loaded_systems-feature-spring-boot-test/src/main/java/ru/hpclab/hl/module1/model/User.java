package ru.hpclab.hl.module1.model;


import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Data
public class User {

    @NonNull
    private UUID identifier;
    @NonNull
    private String fio;

    @NonNull
    private String login;
    @NonNull
    private String email;

    @NonNull
    private LocalDateTime dateRegistry;


    public User(@NonNull String fio, @NonNull String login,  @NonNull String email) {
        this.identifier = UUID.randomUUID();
        this.fio = fio;
        this.login = login;
        this.email = email;
        dateRegistry = LocalDateTime.now();
    }

    public User() {
    }

    @NonNull
    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NonNull UUID identifier) {
        this.identifier = identifier;
    }

    @NonNull
    public String getFio() {
        return fio;
    }

    @NonNull
    public String getLogin() {
        return login;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public LocalDateTime getDateRegistry() {
        return dateRegistry;
    }

    public void setFio(@NonNull String fio) {
        this.fio = fio;
    }

    @Override
    public String toString() {
        return "User{" +
                "identifier=" + identifier +
                ", fio='" + fio + '\'' +
                '}';
    }
}
