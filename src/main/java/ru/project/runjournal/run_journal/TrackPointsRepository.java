package ru.project.runjournal.run_journal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackPointsRepository extends CrudRepository<TrackPoints,Long> {

}
