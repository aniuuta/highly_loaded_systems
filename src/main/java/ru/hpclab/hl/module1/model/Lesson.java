package ru.hpclab.hl.module1.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "seconds", nullable = false)
    private int seconds;

    @Column(name = "name_test", nullable = false)
    private String nameTest;

    public Lesson(String title, int seconds, String nameTest) {
        this.title = title;
        this.seconds = seconds;
        this.nameTest = nameTest;
    }

}
