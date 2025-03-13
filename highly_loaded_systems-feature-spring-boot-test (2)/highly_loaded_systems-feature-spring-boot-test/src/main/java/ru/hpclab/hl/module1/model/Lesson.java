package ru.hpclab.hl.module1.model;

import java.util.UUID;
import lombok.*;

@Data
@AllArgsConstructor
public class Lesson
{
    private UUID id;

    private String Title;

    private int seconds;

    private String NameTest;
}
