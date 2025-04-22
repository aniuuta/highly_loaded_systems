package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Progress;
import ru.hpclab.hl.module1.model.ProgressTDO;
import ru.hpclab.hl.module1.service.ProgressService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/progresses")
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    // Получить все записи прогресса
    @GetMapping
    public List<Progress> getAllProgresses() {
        return progressService.getAllProgresses();
    }

    // Получить прогресс по ID
    @GetMapping("/{id}")
    public Progress getProgressById(@PathVariable UUID id) {
        return progressService.getProgressById(id);
    }

    // Удалить прогресс по ID
    @DeleteMapping("/{id}")
    public void deleteProgress(@PathVariable UUID id) {
        progressService.deleteProgress(id);
    }

    // Сохранить новый прогресс
    @PostMapping
    public ResponseEntity<Progress> saveProgress(@RequestBody ProgressTDO progress)
    {
        return ResponseEntity.ok(progressService.saveProgress(new Progress(
                progress.getUser(),
                progress.getLesson(),
                progress.getEnding()
        )));
    }

    // Обновить прогресс по ID
    @PutMapping("/{id}")
    public Progress updateProgress(@PathVariable UUID id, @RequestBody Progress progress) {
        return progressService.updateProgress(id, progress);
    }

    // Пользовательские методы

    // Получить прогресс по ID пользователя
    @GetMapping("/user/{userId}")
    public List<Progress> getProgressesByUserId(@PathVariable UUID userId) {
        return progressService.getProgressesByUserId(userId);
    }

    // Получить прогресс по ID урока
    @GetMapping("/lesson/{lessonId}")
    public List<Progress> getProgressesByLessonId(@PathVariable UUID lessonId) {
        return progressService.getProgressesByLessonId(lessonId);
    }

    // Получить прогресс с результатом теста выше определенного значения
    @GetMapping("/test-result/{minResult}")
    public List<Progress> getProgressesWithTestResultGreaterThan(@PathVariable int minResult) {
        return progressService.getProgressesWithTestResultGreaterThan(minResult);
    }

    // Получить прогресс по дате завершения (после определенной даты)
    @GetMapping("/after-ending")
    public List<Progress> getProgressesAfterEndingDate(@RequestParam LocalDateTime date) {
        return progressService.getProgressesAfterEndingDate(date);
    }
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllProgress() {
        progressService.clearAll();
        return ResponseEntity.ok().build();
    }
}