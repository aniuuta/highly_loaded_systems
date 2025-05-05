package ru.hpclab.hl.module1.model;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.lang.NonNull;
@Data
public class UserTDO
{
    private String fio;

    private String login;

    private String email;
}

