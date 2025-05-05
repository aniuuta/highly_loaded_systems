package ru.hpclab.hl.module1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class ObservabilityService {

    private final Map<String, MetricStats> metrics = new ConcurrentHashMap<>();

    public void recordTiming(String metricName, long durationMs) {
        metrics.computeIfAbsent(metricName, k -> new MetricStats())
                .record(durationMs);
    }

    @Scheduled(fixedRate = 10_000)
    public void logMetricsReport() {
        if (metrics.isEmpty()) {
            log.info("No metrics collected yet");
            return;
        }

        long now = System.currentTimeMillis();

        log.info("\n=== Performance Metrics Report ===");
        logPeriodStats("Last 10 seconds", 10_000, now);
        logPeriodStats("Last 30 seconds", 30_000, now);
        logPeriodStats("Last 1 minute", 60_000, now);
        log.info("==================================");
    }

    private void logPeriodStats(String periodName, long periodMs, long now) {
        log.info("\n--- {} ---", periodName);
        metrics.forEach((name, stats) -> {
            MetricStats.Snapshot snapshot = stats.getSnapshot(now, periodMs);
            if (snapshot.count() > 0) {
                log.info("[{}] count={}, avg={}ms, min={}ms, max={}ms",
                        name,
                        snapshot.count(),
                        snapshot.avg(),
                        snapshot.min(),
                        snapshot.max());
            }
        });
    }

    public MetricStats getMetricStats(String metricName) {
        return metrics.get(metricName);
    }

    public static class MetricStats {
        private static final long RETENTION_MS = 120_000;

        private final CopyOnWriteArrayList<TimedEntry> entries = new CopyOnWriteArrayList<>();

        public void record(long durationMs) {
            entries.add(new TimedEntry(System.currentTimeMillis(), durationMs));
        }

        public Snapshot getSnapshot(long now, long periodMs) {
            long threshold = now - periodMs;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            long total = 0;
            long count = 0;

            for (TimedEntry entry : entries) {
                if (entry.timestamp >= threshold) {
                    count++;
                    total += entry.duration;
                    min = Math.min(min, entry.duration);
                    max = Math.max(max, entry.duration);
                }
            }

            // Очистка устаревших метрик
            entries.removeIf(e -> e.timestamp < now - RETENTION_MS);

            return count > 0
                    ? new Snapshot(count, total / count, min, max)
                    : new Snapshot(0, 0, 0, 0);
        }

        private static class TimedEntry {
            final long timestamp;
            final long duration;

            TimedEntry(long timestamp, long duration) {
                this.timestamp = timestamp;
                this.duration = duration;
            }
        }

        public record Snapshot(long count, long avg, long min, long max) {}
    }
}
