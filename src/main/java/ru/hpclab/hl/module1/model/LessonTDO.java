package ru.hpclab.hl.module1.model;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class LessonTDO
{
    private String title;

    private int seconds;

    private String nameTest;
}
