package ru.hpclab.hl.module1.repository;

import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.Progress;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryProgressRepository {
    private final Map<UUID, Progress> progresses = new ConcurrentHashMap<>();
    private final Map<UUID, List<UUID>> userProgressesMap = new ConcurrentHashMap<>();
    private final Map<UUID, List<UUID>> lessonProgressesMap = new ConcurrentHashMap<>();

    public List<Progress> findAll() {
        return new ArrayList<>(progresses.values());
    }

    public Optional<Progress> findById(UUID id) {
        return Optional.ofNullable(progresses.get(id));
    }

    public Progress save(Progress progress) {
        if (progress.getId() == null) {
            progress.setId(UUID.randomUUID());
        }

        progresses.put(progress.getId(), progress);

        // Обновляем индексы для быстрого поиска
        userProgressesMap.computeIfAbsent(progress.getUser(), k -> new ArrayList<>()).add(progress.getId());
        lessonProgressesMap.computeIfAbsent(progress.getLesson(), k -> new ArrayList<>()).add(progress.getId());

        return progress;
    }

    public void deleteById(UUID id) {
        Progress progress = progresses.get(id);
        if (progress != null) {
            progresses.remove(id);
            userProgressesMap.getOrDefault(progress.getUser(), Collections.emptyList()).remove(id);
            lessonProgressesMap.getOrDefault(progress.getLesson(), Collections.emptyList()).remove(id);
        }
    }

    public List<Progress> findByUserId(UUID userId) {
        return userProgressesMap.getOrDefault(userId, Collections.emptyList())
                .stream()
                .map(progresses::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Progress> findByLessonId(UUID lessonId) {
        return lessonProgressesMap.getOrDefault(lessonId, Collections.emptyList())
                .stream()
                .map(progresses::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Progress> findByTestResultGreaterThan(int minResult) {
        return progresses.values().stream()
                .filter(p -> p.getTestResult() > minResult)
                .collect(Collectors.toList());
    }

    public List<Progress> findByEndingAfter(LocalDateTime date) {
        return progresses.values().stream()
                .filter(p -> p.getEnding() != null)
                .filter(p -> p.getEnding().isAfter(date))
                .collect(Collectors.toList());
    }

    public Progress update(Progress progress) {
        if (!progresses.containsKey(progress.getId())) {
            throw new NoSuchElementException("Progress not found");
        }

        Progress existing = progresses.get(progress.getId());

        // Если изменился user или lesson, обновляем индексы
        if (!existing.getUser().equals(progress.getUser())) {
            userProgressesMap.getOrDefault(existing.getUser(), Collections.emptyList()).remove(existing.getId());
            userProgressesMap.computeIfAbsent(progress.getUser(), k -> new ArrayList<>()).add(progress.getId());
        }

        if (!existing.getLesson().equals(progress.getLesson())) {
            lessonProgressesMap.getOrDefault(existing.getLesson(), Collections.emptyList()).remove(existing.getId());
            lessonProgressesMap.computeIfAbsent(progress.getLesson(), k -> new ArrayList<>()).add(progress.getId());
        }

        progresses.put(progress.getId(), progress);
        return progress;
    }
}