package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import ru.hpclab.hl.module1.model.Progress;

import java.time.LocalDateTime;
import java.util.*;

public class ProgressService {


    @Value("${statisticsservice.infostring:lines}")
    private String infoString;

    private final ProgressService progressService;

    public ProgressService(ProgressService progressService) {
        this.progressService = progressService;
    }

    public void addProgress(UUID user, UUID lesson, LocalDateTime ending, int testResult) {
        progressService.addProgress(user,lesson,ending,testResult);
    }

    // Удаление прогресса по ID
    public void deleteProgress(UUID id) {
        progressService.deleteProgress(id);
    }

    // Поиск прогресса по ID
    public Progress findProgressById(UUID id)
    {
        return progressService.findProgressById(id);
    }

    // Поиск прогресса по пользователю
    public List<Progress> findProgressByUser(UUID userId) {
        List<Progress> userProgress = new ArrayList<>();
        for (Progress progress : progressService.findProgressById(id)) {
            if (progress.getUser().equals(userId)) {
                userProgress.add(progress);
            }
        }
        return userProgress;
    }



}
