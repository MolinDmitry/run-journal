package ru.project.runjournal.run_journal.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.project.runjournal.run_journal.Entities.Activities;

@Repository
public interface ActivitiesRepository extends CrudRepository<Activities,Long>{
    List<Activities> findByTrackIdAndUserId(long trackId, long userId);
    List<Activities> findByUserIdOrderByActivityDateDesc(long userId); // сортировка в обратном порядке
    

}
