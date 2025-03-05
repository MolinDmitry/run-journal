package ru.project.runjournal.run_journal;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @brief Сущность точки трека
 */

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class TrackPoints {
    @Id
    @SequenceGenerator(name = "TRACKPOINTS_SEQ", sequenceName =  "trackpoints_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRACKPOINTS_SEQ")
    private long id;
    private final double latitude;
    private final double longitude;
    private LocalDateTime time;
    private float speed;
    private float cadence;
    private short hr;
    private Long trackId;

}
