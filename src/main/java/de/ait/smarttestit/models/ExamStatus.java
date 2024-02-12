package de.ait.smarttestit.models;

import java.util.Optional;

public enum ExamStatus {
    PLANNED,
    UNDERWAY,
    COMPLETED;

    public static Optional<ExamStatus> findByName(String name) {
        for (ExamStatus currentStatus : ExamStatus.values()) {
            if (currentStatus.name().equalsIgnoreCase(name)) {
                return Optional.of(currentStatus);
            }
        }
        return Optional.empty();
    }
}
