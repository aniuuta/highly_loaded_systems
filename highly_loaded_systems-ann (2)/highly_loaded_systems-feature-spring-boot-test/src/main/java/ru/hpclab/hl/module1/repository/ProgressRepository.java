package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hpclab.hl.module1.model.Progress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProgressRepository extends JpaRepository<Progress, UUID> {

    // Пользовательский запрос для поиска прогресса по ID пользователя
    @Query("SELECT p FROM Progress p WHERE p.user = :userId")
    List<Progress> findByUserId(@Param("userId") UUID userId);

    // Пользовательский запрос для поиска прогресса по ID урока
    @Query("SELECT p FROM Progress p WHERE p.lesson = :lessonId")
    List<Progress> findByLessonId(@Param("lessonId") UUID lessonId);

    // Пользовательский запрос для поиска прогресса с результатом теста выше определенного значения
    @Query("SELECT p FROM Progress p WHERE p.testResult > :minResult")
    List<Progress> findByTestResultGreaterThan(@Param("minResult") int minResult);

    // Пользовательский запрос для поиска прогресса по дате завершения (после определенной даты)
    @Query("SELECT p FROM Progress p WHERE p.ending > :date")
    List<Progress> findByEndingAfter(@Param("date") LocalDateTime date);
}