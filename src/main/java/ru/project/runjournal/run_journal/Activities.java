package ru.project.runjournal.run_journal;

import java.time.LocalDateTime;
import java.util.List;

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
 * @brief Сущность тренировки
 * 
 */

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class Activities{
    @Id
    @SequenceGenerator(name = "ACTIVITIES_SEQ", sequenceName =  "activities_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITIES_SEQ")
    private Long id;
    private final LocalDateTime activityDate;
    private final String activityType;
    private final String activityCaption;
    private final List<String> activityComment;
    private final int activityDuration; // длительность в секундах
    private final double activityDistance; // дистанция в км
    private final long trackId;
}
