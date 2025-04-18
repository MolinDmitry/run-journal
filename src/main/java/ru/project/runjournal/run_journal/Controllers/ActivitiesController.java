package ru.project.runjournal.run_journal.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.project.runjournal.run_journal.DataProcessing.ActivityDataProcessor;
import ru.project.runjournal.run_journal.Entities.Activities;
import ru.project.runjournal.run_journal.Entities.Users;
import ru.project.runjournal.run_journal.Repositories.ActivitiesRepository;

@Controller
@RequestMapping("/run-journal")
public class ActivitiesController {

    private final ActivitiesRepository activityRepo;

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

    @Autowired
    public ActivitiesController(ActivitiesRepository activityRepository){
        this.activityRepo = activityRepository;
    } 
    
    

    @ModelAttribute
    public void addUserDataToModel(@AuthenticationPrincipal Users currentUser, Model model){
        model.addAttribute("currentUsername", currentUser.getUsername());
        model.addAttribute("currentFirstName", currentUser.getFirstName());
        
    }

    @ModelAttribute(name ="activityBDList")
    public List<ActivityBriefData>  addActivityList(@AuthenticationPrincipal Users currentUser){
        List<ActivityBriefData> activityBDList = new ArrayList<>();
        List<Activities> activityList = activityRepo.findByUserIdOrderByActivityDateDesc(currentUser.getId());
        if (!activityList.isEmpty()){
            activityList.stream().forEach(
                activity -> {
                    activityBDList.add(
                        new ActivityBriefData(
                            ActivityDataProcessor.convertActivityDateToString(activity),
                            activity.getActivityCaption(),
                            ActivityDataProcessor.convertActivityTypeToRusString(activity),
                            ActivityDataProcessor.convertActivityDurationToString(activity.getActivityDuration(),false),
                            ActivityDataProcessor.convertActivityDistanceToString(activity) + " km",
                            activity.getId()
                        ));
                });            
        }
        return activityBDList;
    }

    @GetMapping
    public String showActivitiesPage(){
        return "activities";
    }

}
