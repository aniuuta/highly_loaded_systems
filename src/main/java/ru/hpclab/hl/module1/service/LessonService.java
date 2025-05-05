package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Lesson;
import ru.hpclab.hl.module1.repository.LessonRepository;

import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ObservabilityService observabilityService;

    public LessonService(LessonRepository lessonRepository,
                         ObservabilityService observabilityService) {
        this.lessonRepository = lessonRepository;
        this.observabilityService = observabilityService;
    }

    public List<Lesson> getAllLessons() {
        return measure("service.lesson.get_all", (MeasurableOperation<List<Lesson>>) lessonRepository::findAll);

    }

    public Lesson getLessonById(long id) {
        return measure("service.lesson.get_by_id", () ->
                lessonRepository.findById(id).orElse(null));
    }

    public Lesson saveLesson(Lesson lesson) {
        return measure("service.lesson.save", () -> lessonRepository.save(lesson));
    }

    public void deleteLesson(long id) {
        measure("service.lesson.delete", () -> lessonRepository.deleteById(id));
    }

    public Lesson updateLesson(long id, Lesson lesson) {
        return measure("service.lesson.update", () -> {
            lesson.setId(id);
            return lessonRepository.save(lesson);
        });
    }

    public void clearAll() {
        measure("service.lesson.clear_all", (Runnable) lessonRepository::deleteAll);
    }

    private <T> T measure(String metricName, MeasurableOperation<T> operation) {
        long startTime = System.currentTimeMillis();
        try {
            return operation.execute();
        } finally {
            observabilityService.recordTiming(metricName,
                    System.currentTimeMillis() - startTime);
        }
    }

    private void measure(String metricName, Runnable operation) {
        long startTime = System.currentTimeMillis();
        try {
            operation.run();
        } finally {
            observabilityService.recordTiming(metricName,
                    System.currentTimeMillis() - startTime);
        }
    }

    @FunctionalInterface
    private interface MeasurableOperation<T> {
        T execute();
    }
}