package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Lesson;
import ru.hpclab.hl.module1.model.LessonTDO;
import ru.hpclab.hl.module1.service.LessonService;
import ru.hpclab.hl.module1.service.ObservabilityService;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final ObservabilityService observabilityService;

    @Autowired
    public LessonController(LessonService lessonService, ObservabilityService observabilityService) {
        this.lessonService = lessonService;
        this.observabilityService = observabilityService;
    }

    private <T> T measure(String metricName, java.util.function.Supplier<T> operation) {
        long start = System.currentTimeMillis();
        try {
            return operation.get();
        } finally {
            observabilityService.recordTiming(metricName, System.currentTimeMillis() - start);
        }
    }

    private void measure(String metricName, Runnable operation) {
        long start = System.currentTimeMillis();
        try {
            operation.run();
        } finally {
            observabilityService.recordTiming(metricName, System.currentTimeMillis() - start);
        }
    }

    @GetMapping
    public List<Lesson> getAllLessons() {
        return measure("controller.lesson.get_all", lessonService::getAllLessons);
    }

    @GetMapping("/{id}")
    public Lesson getLessonById(@PathVariable long id) {
        return measure("controller.lesson.get_by_id", () -> lessonService.getLessonById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable long id) {
        measure("controller.lesson.delete", () -> lessonService.deleteLesson(id));
    }

    @PostMapping
    public ResponseEntity<Lesson> saveLesson(@RequestBody LessonTDO lesson) {
        return measure("controller.lesson.save", () -> ResponseEntity.ok(
                lessonService.saveLesson(new Lesson(
                        lesson.getTitle(),
                        lesson.getSeconds(),
                        lesson.getNameTest()
                ))
        ));
    }

    @PutMapping("/{id}")
    public Lesson updateLesson(@PathVariable long id, @RequestBody Lesson lesson) {
        return measure("controller.lesson.update", () -> lessonService.updateLesson(id, lesson));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllLessons() {
        measure("controller.lesson.clear_all", lessonService::clearAll);
        return ResponseEntity.ok().build();
    }
}
