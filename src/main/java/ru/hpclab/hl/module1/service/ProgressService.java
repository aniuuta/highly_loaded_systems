package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Progress;
import ru.hpclab.hl.module1.repository.ProgressRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;

    @Autowired
    public ProgressService(ProgressRepository progressRepository)
    {
        System.out.println("Repository injected: " + (progressRepository != null));
        this.progressRepository = progressRepository;
    }

    // Получить все записи прогресса
    public List<Progress> getAllProgresses() {
        return progressRepository.findAll();
    }

    // Получить прогресс по ID
    public Progress getProgressById(UUID id) {
        return progressRepository.findById(id).orElse(null);
    }

    // Сохранить новый прогресс
    public Progress saveProgress(Progress progress) {
        return progressRepository.save(progress);
    }

    // Удалить прогресс по ID
    public void deleteProgress(UUID id) {
        progressRepository.deleteById(id);
    }

    // Обновить прогресс по ID
    public Progress updateProgress(UUID id, Progress progress) {
        progress.setId(id);
        return progressRepository.save(progress);
    }

    // Пользовательские методы на JPA

    // Получить прогресс по ID пользователя
    public List<Progress> getProgressesByUserId(UUID userId) {
        return progressRepository.findByUserId(userId);
    }

    // Получить прогресс по ID урока
    public List<Progress> getProgressesByLessonId(UUID lessonId) {
        return progressRepository.findByLessonId(lessonId);
    }

    // Получить прогресс с результатом теста выше определенного значения
    public List<Progress> getProgressesWithTestResultGreaterThan(int minResult) {
        return progressRepository.findByTestResultGreaterThan(minResult);
    }

    // Получить прогресс по дате завершения (например, после определенной даты)
    public List<Progress> getProgressesAfterEndingDate(LocalDateTime date) {
        return progressRepository.findByEndingAfter(date);
    }
    public void clearAll() {
        progressRepository.deleteAll();
    }
}