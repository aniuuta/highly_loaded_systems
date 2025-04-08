package ru.hpclab.hl.module1.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "seconds", nullable = false)
    private int seconds;

    @Column(name = "name_test", nullable = false)
    private String nameTest;

    public Lesson(@org.springframework.lang.NonNull String title,
                  @org.springframework.lang.NonNull int seconds,
                  @NonNull String nameTest)
    {
        this.title = title;
        this.seconds = seconds;
        this.nameTest = nameTest;
    }
}