package ru.hpclab.hl.module1.metrics;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;


public class TimingStats {
    private final ConcurrentLinkedDeque<TimeRecord> records = new ConcurrentLinkedDeque<>();

    public void addRecord(long durationMs) {
        records.add(new TimeRecord(durationMs, System.currentTimeMillis()));
        cleanOldRecords();
    }

    public StatsSnapshot getStats(long periodMs) {
        cleanOldRecords();
        long cutoffTime = System.currentTimeMillis() - periodMs;

        AtomicLong count = new AtomicLong();
        AtomicLong total = new AtomicLong();
        AtomicLong min = new AtomicLong(Long.MAX_VALUE);
        AtomicLong max = new AtomicLong(Long.MIN_VALUE);

        records.stream()
                .filter(r -> r.timestamp >= cutoffTime)
                .forEach(r -> {
                    count.incrementAndGet();
                    total.addAndGet(r.duration);
                    min.set(Math.min(min.get(), r.duration));
                    max.set(Math.max(max.get(), r.duration));
                });

        if (count.get() == 0) {
            return new StatsSnapshot(0, 0, 0, 0);
        }

        return new StatsSnapshot(
                count.get(),
                total.get() / count.get(),
                min.get(),
                max.get()
        );
    }

    private void cleanOldRecords() {
        long oneMinuteAgo = System.currentTimeMillis() - 60_000;
        while (!records.isEmpty() && records.peekFirst().timestamp < oneMinuteAgo) {
            records.pollFirst();
        }
    }

    private static class TimeRecord {
        final long duration;
        final long timestamp;

        TimeRecord(long duration, long timestamp) {
            this.duration = duration;
            this.timestamp = timestamp;
        }
    }

    public static class StatsSnapshot {
        public final long count;
        public final long avg;
        public final long min;
        public final long max;

        StatsSnapshot(long count, long avg, long min, long max) {
            this.count = count;
            this.avg = avg;
            this.min = min;
            this.max = max;
        }
    }
}