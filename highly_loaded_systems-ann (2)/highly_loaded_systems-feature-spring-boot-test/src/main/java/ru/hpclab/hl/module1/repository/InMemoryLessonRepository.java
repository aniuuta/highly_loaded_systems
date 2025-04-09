package ru.hpclab.hl.module1.repository;

import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.Lesson;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryLessonRepository {
    private final Map<UUID, Lesson> lessons = new ConcurrentHashMap<>();

    public List<Lesson> findAll() {
        return new ArrayList<>(lessons.values());
    }

    public Optional<Lesson> findById(UUID id) {
        return Optional.ofNullable(lessons.get(id));
    }

    public Lesson save(Lesson lesson) {
        if (lesson.getId() == null) {
            lesson.setId(UUID.randomUUID());
        }
        lessons.put(lesson.getId(), lesson);
        return lesson;
    }

    public void deleteById(UUID id) {
        lessons.remove(id);
    }

    public Lesson update(Lesson lesson) {
        if (!lessons.containsKey(lesson.getId())) {
            throw new NoSuchElementException("Lesson not found");
        }
        lessons.put(lesson.getId(), lesson);
        return lesson;
    }
}