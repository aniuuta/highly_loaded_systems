package ru.hpclab.hl.module1.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "ending", nullable = false)
    private LocalDateTime ending;

    @Column(name = "test_result", nullable = false)
    private int testResult;

    public Progress(User user, Lesson lesson, LocalDateTime ending, int testResult) {
        this.user = user;
        this.lesson = lesson;
        this.ending = ending;
        this.testResult = testResult;
    }

}
