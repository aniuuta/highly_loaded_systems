package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hpclab.hl.module1.model.Progress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
}