package ru.project.runjournal.run_journal.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.project.runjournal.run_journal.Entities.Users;

@Repository
public interface UsersRepository extends CrudRepository<Users,Long>{
    Optional<Users> findByUsername(String username);

}
