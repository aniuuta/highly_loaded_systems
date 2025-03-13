package ru.hpclab.hl.module1.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.*;

@Data
@AllArgsConstructor
public class Progress
{
    private UUID id;

    private UUID user;

    private UUID lesson;

    private LocalDateTime ending;

    private int testResult;
}
