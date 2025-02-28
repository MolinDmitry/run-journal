package ru.project.runjournal.run_journal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @brief Главный класс приложения
 * 
 * Основная страница открывается по пути "/activities"
 */
@SpringBootApplication
public class RunJournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunJournalApplication.class, args);
	}

}
