package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Progress;
import ru.hpclab.hl.module1.model.ProgressTDO;
import ru.hpclab.hl.module1.service.ProgressService;

import java.util.List;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    // Получить весь прогресс в формате TDO
    @GetMapping
    public ResponseEntity<List<ProgressTDO>> getAllProgress() {
        return ResponseEntity.ok(progressService.getAllProgress());
    }

    // Получить прогресс по ID в формате TDO
    @GetMapping("/{id}")
    public ResponseEntity<ProgressTDO> getProgressById(@PathVariable Long id) {
        try {
            ProgressTDO progress = progressService.getProgressById(id);
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Сохранить новый прогресс
    @PostMapping
    public ResponseEntity<ProgressTDO> saveProgress(@RequestBody ProgressTDO progressTDO) {
        // Сохраняем прогресс
        ProgressTDO savedProgress = progressService.saveProgress(progressTDO);

        // Возвращаем сохраненный объект с HTTP статусом 200 OK
        return ResponseEntity.ok(savedProgress);
    }

    // Обновить прогресс по ID
    @PutMapping("/{id}")
    public ResponseEntity<Progress> updateProgress(@PathVariable Long id, @RequestBody ProgressTDO progressTDO) {
        Progress updatedProgress = progressService.convertToEntity(progressTDO);
        return ResponseEntity.ok(progressService.updateProgress(id, updatedProgress));
    }

    // Удалить прогресс по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id) {
        progressService.deleteProgress(id);
        return ResponseEntity.ok().build();
    }

    // Удалить весь прогресс
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllProgress() {
        progressService.clearAll();
        return ResponseEntity.ok().build();
    }
}
