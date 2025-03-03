package ru.project.runjournal.run_journal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.Data;

@Controller
@RequestMapping("/")
public class ActivitiesController {
    
    @Data
    @AllArgsConstructor
    private class ActivityBriefData{
        String activityDate;
        String activityCaption;
        String activityType;
        String activityDuration;
        String activityDistance;
        Long activityId;
    }
    

    @ModelAttribute
    public void addUserDataToModel(Model model){
        model.addAttribute("currentUsername", "dmitry");
        model.addAttribute("currentFirstName", "Дмитрий");
    }

    @GetMapping
    public String showActivitiesPage(){
        return "activities";
    }

}
