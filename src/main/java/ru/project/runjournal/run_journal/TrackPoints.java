package ru.project.runjournal.run_journal;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @brief Сущность точки трека
 */

@Data
@AllArgsConstructor
public class TrackPoints {
    private double latitude;
    private double longitude;
    private LocalDateTime time;
    private float speed;
    private float cadence;
    private short hr;
    private Long trackId;

}
