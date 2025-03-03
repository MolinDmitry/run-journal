package ru.project.runjournal.run_journal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public void addUserDataToModel(@AuthenticationPrincipal Users currentUser, Model model){
        model.addAttribute("currentUsername", currentUser.getUsername());
        model.addAttribute("currentFirstName", currentUser.getFirstName());
    }

    @ModelAttribute(name ="activityList")
    public List<ActivityBriefData>  addActivityList(){
        List<ActivityBriefData> activityList = new ArrayList<>();
        activityList.add(new ActivityBriefData(
            "01-март-2025",
            "Дневной бег",
            "Бег",
            "0h 32m",
            "5.2",
            1024l
        ));
        activityList.add(new ActivityBriefData(
            "02-март-2025",
            "Утренний бег",
            "Бег",
            "1h 02m",
            "10.1",
            1025l
        ));
        activityList.add(new ActivityBriefData(
            "03-март-2025",
            "Вечерний бег",
            "Бег",
            "0h 52m",
            "8.5",
            1026l
        ));
        return activityList;
    }

    @GetMapping
    public String showActivitiesPage(){
        return "activities";
    }

}
