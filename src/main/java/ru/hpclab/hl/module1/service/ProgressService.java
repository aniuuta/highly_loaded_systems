package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hpclab.hl.module1.model.Lesson;
import ru.hpclab.hl.module1.model.Progress;
import ru.hpclab.hl.module1.model.ProgressTDO;
import ru.hpclab.hl.module1.model.User;
import ru.hpclab.hl.module1.repository.LessonRepository;
import ru.hpclab.hl.module1.repository.ProgressRepository;
import ru.hpclab.hl.module1.repository.UserRepository;
import ru.hpclab.hl.module1.service.ObservabilityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final ObservabilityService observabilityService;

    @Autowired
    public ProgressService(ProgressRepository progressRepository,
                           UserRepository userRepository,
                           LessonRepository lessonRepository,
                           ObservabilityService observabilityService) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.observabilityService = observabilityService;
    }

    public Progress convertToEntity(ProgressTDO progressTDO) {
        long start = System.currentTimeMillis();
        try {
            User user = userRepository.findById(progressTDO.getUser())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Lesson lesson = lessonRepository.findById(progressTDO.getLesson())
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));

            Progress progress = new Progress();
            progress.setUser(user);
            progress.setLesson(lesson);
            progress.setEnding(progressTDO.getEnding());
            progress.setTestResult(progressTDO.getTestResult());
            return progress;
        } finally {
            observabilityService.recordTiming("service.progress.convert_to_entity", System.currentTimeMillis() - start);
        }
    }

    public ProgressTDO convertToDTO(Progress progress) {
        long start = System.currentTimeMillis();
        try {
            ProgressTDO progressTDO = new ProgressTDO();
            progressTDO.setId(progress.getId());
            progressTDO.setUser(progress.getUser().getId());
            progressTDO.setLesson(progress.getLesson().getId());
            progressTDO.setEnding(progress.getEnding());
            progressTDO.setTestResult(progress.getTestResult());
            return progressTDO;
        } finally {
            observabilityService.recordTiming("service.progress.convert_to_dto", System.currentTimeMillis() - start);
        }
    }

    public List<ProgressTDO> getAllProgress() {
        long start = System.currentTimeMillis();
        try {
            List<Progress> progressList = progressRepository.findAll();
            return progressList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } finally {
            observabilityService.recordTiming("service.progress.get_all", System.currentTimeMillis() - start);
        }
    }

    public ProgressTDO getProgressById(Long id) {
        long start = System.currentTimeMillis();
        try {
            Progress progress = progressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Прогресс с id " + id + " не найден"));
            return convertToDTO(progress);
        } finally {
            observabilityService.recordTiming("service.progress.get_by_id", System.currentTimeMillis() - start);
        }
    }

    public ProgressTDO saveProgress(ProgressTDO progressTDO) {
        long start = System.currentTimeMillis();
        try {
            Progress progress = convertToEntity(progressTDO);
            Progress saved = progressRepository.save(progress);
            return convertToDTO(saved);
        } finally {
            observabilityService.recordTiming("service.progress.save", System.currentTimeMillis() - start);
        }
    }

    public Progress updateProgress(Long id, Progress updatedProgress) {
        long start = System.currentTimeMillis();
        try {
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
        } finally {
            observabilityService.recordTiming("service.progress.update", System.currentTimeMillis() - start);
        }
    }

    public void deleteProgress(Long id) {
        long start = System.currentTimeMillis();
        try {
            progressRepository.deleteById(id);
        } finally {
            observabilityService.recordTiming("service.progress.delete", System.currentTimeMillis() - start);
        }
    }

    @Transactional
    public void clearAll() {
        long start = System.currentTimeMillis();
        try {
            progressRepository.deleteAll();
        } finally {
            observabilityService.recordTiming("service.progress.clear_all", System.currentTimeMillis() - start);
        }
    }
}
