package ru.project.runjournal.run_journal;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users,Long>{
    Optional<Users> findByUsername(String username);

}
