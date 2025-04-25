package ru.hpclab.hl.module1.model;

import java.time.LocalDateTime;

public class ProgressTDO {
    private long user;
    private long lesson;
    private LocalDateTime ending;
    private int testResult; // ← добавили результат теста

    // Getters and Setters
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
