package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Lesson;
import ru.hpclab.hl.module1.model.Progress;
import ru.hpclab.hl.module1.model.ProgressTDO;
import ru.hpclab.hl.module1.model.User;
import ru.hpclab.hl.module1.repository.LessonRepository;
import ru.hpclab.hl.module1.repository.ProgressRepository;
import ru.hpclab.hl.module1.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public ProgressService(ProgressRepository progressRepository, UserRepository userRepository, LessonRepository lessonRepository) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    public Progress convertToEntity(ProgressTDO progressTDO) {
        User user = userRepository.findById(progressTDO.getUser())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lesson lesson = lessonRepository.findById(progressTDO.getLesson())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Progress progress = new Progress();
        progress.setUser(user);
        progress.setLesson(lesson);
        progress.setEnding(progressTDO.getEnding());
        progress.setTestResult(0);  // Начальная оценка, можно установить другое значение по умолчанию

        return progress;
    }

    // Конвертировать Progress в ProgressTDO
    public ProgressTDO convertToDTO(Progress progress) {
        ProgressTDO progressTDO = new ProgressTDO();
        progressTDO.setUser(progress.getUser().getId());
        progressTDO.setLesson(progress.getLesson().getId());
        progressTDO.setEnding(progress.getEnding());
        progressTDO.setTestResult(progress.getTestResult()); // <--- добавили это
        return progressTDO;
    }


    // Получить весь прогресс в виде ProgressTDO
    public List<ProgressTDO> getAllProgress() {
        List<Progress> progressList = progressRepository.findAll();
        return progressList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Получить прогресс по ID в виде ProgressTDO
    public ProgressTDO getProgressById(Long id) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Прогресс с id " + id + " не найден"));
        return convertToDTO(progress);
    }

    // Сохранить новый прогресс
    public Progress saveProgress(ProgressTDO progressTDO) {
        // Преобразуем ProgressTDO в Progress
        Progress progress = convertToEntity(progressTDO);
        // Сохраняем прогресс в базе данных
        return progressRepository.save(progress);
    }


    // Обновить прогресс по ID
    public Progress updateProgress(Long id, Progress updatedProgress) {
        Optional<Progress> existingProgressOpt = progressRepository.findById(id);
        if (existingProgressOpt.isPresent()) {
            Progress existing = existingProgressOpt.get();
            existing.setUser(updatedProgress.getUser());
            existing.setLesson(updatedProgress.getLesson());
            existing.setEnding(updatedProgress.getEnding());
            existing.setTestResult(updatedProgress.getTestResult());
            return progressRepository.save(existing);
        } else {
            throw new RuntimeException("Прогресс с id " + id + " не найден");
        }
    }

    // Удалить прогресс по ID
    public void deleteProgress(Long id) {
        progressRepository.deleteById(id);
    }

    // Удалить весь прогресс
    public void clearAll() {
        progressRepository.deleteAll();
    }
}
