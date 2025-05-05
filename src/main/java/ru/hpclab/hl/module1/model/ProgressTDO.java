package ru.hpclab.hl.module1.model;

import java.time.LocalDateTime;

public class ProgressTDO {
    private long id;
    private long user;
    private long lesson;
    private LocalDateTime ending;
    private int testResult; // ← добавили результат теста

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getLesson() {
        return lesson;
    }

    public void setLesson(long lesson) {
        this.lesson = lesson;
    }

    public LocalDateTime getEnding() {
        return ending;
    }

    public void setEnding(LocalDateTime ending) {
        this.ending = ending;
    }

    public int getTestResult() {
        return testResult;
    }

    public void setTestResult(int testResult) {
        this.testResult = testResult;
    }
}
