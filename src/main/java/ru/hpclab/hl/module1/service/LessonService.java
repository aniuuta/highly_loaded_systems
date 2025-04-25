package ru.hpclab.hl.module1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.model.Lesson;
import ru.hpclab.hl.module1.repository.LessonRepository;

import java.util.List;
import java.util.UUID;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository)
    {
        System.out.println("Repository injected: " + (lessonRepository != null));
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson getLessonById(long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public void deleteLesson(long id) {
        lessonRepository.deleteById(id);
    }

    public Lesson updateLesson(long id, Lesson lesson) {
        lesson.setId(id);
        return lessonRepository.save(lesson);
    }
    public void clearAll() {
        lessonRepository.deleteAll();
    }
}