package ru.hpclab.hl.module1.repository;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import ru.hpclab.hl.module1.controller.exeption.LessonException;
import ru.hpclab.hl.module1.model.Lesson;

import java.util.*;

import static java.lang.String.format;

@Repository
public class LessonRepository {

    public static final String LESSON_NOT_FOUND_MSG = "Lesson with ID %s not found";
    public static final String LESSON_EXISTS_MSG = "Lesson with ID %s is already exists";

    private final Map<UUID, Lesson> lessons = new HashMap<>();

    public List<Lesson> findAll() {
        return new ArrayList<>(lessons.values());
    }

    public Lesson findById(UUID id) {
        final var lesson = lessons.get(id);
        if (lesson == null) {
            throw new LessonException(format(LESSON_NOT_FOUND_MSG, id));
        }
        return lesson;
    }

    public void delete(UUID id) {
        final var removed = lessons.remove(id);
        if (removed == null) {
            throw new LessonException(format(LESSON_NOT_FOUND_MSG, id));
        }
    }

    public Lesson save(Lesson lesson) {
        if (ObjectUtils.isEmpty(lesson.getId())) {
            lesson.setId(UUID.randomUUID());
        }

        final var lessonData = lessons.get(lesson.getId());
        if (lessonData != null) {
            throw new LessonException(format(LESSON_EXISTS_MSG, lesson.getId()));
        }

        lessons.put(lesson.getId(), lesson);

        return lesson;
    }

    public Lesson put(Lesson lesson) {
        final var lessonData = lessons.get(lesson.getId());
        if (lessonData == null) {
            throw new LessonException(format(LESSON_NOT_FOUND_MSG, lesson.getId()));
        }

        final var removed = lessons.remove(lesson.getId());
        if (removed != null) {
            lessons.put(lesson.getId(), lesson);
        } else {
            throw new LessonException(format(LESSON_NOT_FOUND_MSG, lesson.getId()));
        }

        return lesson;
    }

    public void clear(){
        lessons.clear();
    }

}
