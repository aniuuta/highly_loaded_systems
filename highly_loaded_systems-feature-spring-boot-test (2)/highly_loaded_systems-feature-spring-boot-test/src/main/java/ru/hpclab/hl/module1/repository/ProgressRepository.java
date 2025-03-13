package ru.hpclab.hl.module1.repository;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import ru.hpclab.hl.module1.controller.exeption.ProgressException;
import ru.hpclab.hl.module1.model.Progress;

import java.util.*;

import static java.lang.String.format;

@Repository
public class ProgressRepository {

    public static final String PROGRESS_NOT_FOUND_MSG = "Progress with ID %s not found";
    public static final String PROGRESS_EXISTS_MSG = "Progress with ID %s is already exists";

    private final Map<UUID, Progress> progresses = new HashMap<>();

    public List<Progress> findAll() {
        return new ArrayList<>(progresses.values());
    }

    public Progress findById(UUID id) {
        final var progress = progresses.get(id);
        if (progress == null) {
            throw new ProgressException(format(PROGRESS_NOT_FOUND_MSG, id));
        }
        return progress;
    }

    public void delete(UUID id) {
        final var removed = progresses.remove(id);
        if (removed == null) {
            throw new ProgressException(format(PROGRESS_NOT_FOUND_MSG, id));
        }
    }

    public Progress save(Progress progress) {
        if (ObjectUtils.isEmpty(progress.getId())) {
            progress.setId(UUID.randomUUID());
        }

        final var progressData = progresses.get(progress.getId());
        if (progressData != null) {
            throw new ProgressException(format(PROGRESS_EXISTS_MSG, progress.getId()));
        }

        progresses.put(progress.getId(), progress);

        return progress;
    }

    public Progress put(Progress progress) {
        final var progressData = progresses.get(progress.getId());
        if (progressData == null) {
            throw new ProgressException(format(PROGRESS_NOT_FOUND_MSG, progress.getId()));
        }

        final var removed = progresses.remove(progress.getId());
        if (removed != null) {
            progresses.put(progress.getId(), progress);
        } else {
            throw new ProgressException(format(PROGRESS_NOT_FOUND_MSG, progress.getId()));
        }

        return progress;
    }

    public void clear(){
        progresses.clear();
    }

}
