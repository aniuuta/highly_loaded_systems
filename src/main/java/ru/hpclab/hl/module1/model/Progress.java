package ru.hpclab.hl.module1.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "progress")
public class Progress {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID user;

    @Column(name = "lesson_id", nullable = false)
    private UUID lesson;

    @Column(name = "ending", nullable = false)
    private LocalDateTime ending;

    @Column(name = "test_result", nullable = false)
    private int testResult;

    public Progress(UUID user, UUID lesson, LocalDateTime ending)
    {
        this.id = UUID.randomUUID();
        this.ending = ending;
        this.lesson = lesson;
        this.testResult = 0;
        this.user = user;
    }
}