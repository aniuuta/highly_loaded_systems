package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Lesson;
import ru.hpclab.hl.module1.model.LessonTDO;
import ru.hpclab.hl.module1.service.LessonService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public List<Lesson> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    public Lesson getLessonById(@PathVariable UUID id) {
        return lessonService.getLessonById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable UUID id) {
        lessonService.deleteLesson(id);
    }

    @PostMapping
    public ResponseEntity<Lesson> saveLesson(@RequestBody LessonTDO lesson)
    {
        return ResponseEntity.ok(lessonService.saveLesson(new Lesson(
                lesson.getTitle(),
                lesson.getSeconds(),
                lesson.getNameTest()
        )));
    }

    @PutMapping("/{id}")
    public Lesson updateLesson(@PathVariable UUID id, @RequestBody Lesson lesson) {
        return lessonService.updateLesson(id, lesson);
    }
}