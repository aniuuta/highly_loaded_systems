package ru.hpclab.hl.module1.model;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProgressTDO
{
    private UUID user;

    private UUID lesson;

    private LocalDateTime ending;

}
