package ru.project.runjournal.run_journal;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.Data;

@Controller
@RequestMapping("/addactivity")
public class ActivityAddingController {

    @Data
    @AllArgsConstructor
    private class NewActivityData{
        private String activityType;
        private List<String> activityComment;
        private List<String> fileGPX;
    }

    @GetMapping
    public String showAddActivityPage(){
        return "addactivity";
    }


    /**
     * @brief Обрабатывает POST запрос от страницы добавления новой тренировки
     * @return Возвращает перенаправление на страницу журнала тренировок
     */
    @PostMapping
    public String processActivityAdding(){

        return "redirect:/";
    }
}
