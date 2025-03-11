package ru.project.runjournal.run_journal.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.project.runjournal.run_journal.Entities.TrackPoints;

@Repository
public interface TrackPointsRepository extends CrudRepository<TrackPoints,Long> {
    List<TrackPoints> findByTrackIdOrderByTime(long trackId);

}
