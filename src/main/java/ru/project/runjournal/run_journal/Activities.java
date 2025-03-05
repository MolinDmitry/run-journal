package ru.project.runjournal.run_journal;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @brief Сущность тренировки
 * 
 */

@Data
@RequiredArgsConstructor
public class Activities{
    private Long id;
    private final LocalDateTime activityDate;
    private final String activityType;
    private final String activityCaption;
    private final List<String> activityComment;
    private final int activityDuration; // длительность в секундах
    private final double activityDistance; // дистанция в км
    private final long trackId;
}
