package ru.project.runjournal.run_journal;


import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

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
        private MultipartFile fileGPX;
    }

    private enum ActivityType {
        RUNNING,
        TRAILRUNNING,
        HIKING,
        TREKKING,
        WALK,
        BIKETOUR,
        BIKERIDE,
        BIKERACE,
        NORDICSKI,
        CLASSICSKI,
        SKATESKI
    }

    @ModelAttribute
    public void addUserDataToModel(@AuthenticationPrincipal Users currentUser, Model model){
        model.addAttribute("currentUsername", currentUser.getUsername());
        model.addAttribute("currentFirstName", currentUser.getFirstName());
    }

    @ModelAttribute void createActivityTypeAttribute(Model model){
        model.addAttribute("activityTypesList", Arrays.asList(ActivityType.values()));
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
    public String processActivityAdding(NewActivityData activityData){
        System.out.println(activityData.getActivityType());
        System.out.println(activityData.getActivityComment().toString());
        if (!activityData.fileGPX.isEmpty()){
            new GpxProcessor().processGPX(activityData.fileGPX);
        }
        else{
             System.out.println("файл пустой");
        }

        return "redirect:/";
    }
}
