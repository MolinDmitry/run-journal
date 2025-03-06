package ru.project.runjournal.run_journal;

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

@Controller
@RequestMapping("/")
public class ActivitiesController {

    private Long curUserId;
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
        this.curUserId = currentUser.getId();
    }

    @ModelAttribute(name ="activityBDList")
    public List<ActivityBriefData>  addActivityList(){
        List<ActivityBriefData> activityBDList = new ArrayList<>();
        List<Activities> activityList = activityRepo.findByUserId(this.curUserId);
        if (!activityList.isEmpty()){
            activityList.stream().forEach(
                activity -> {
                    activityBDList.add(
                        new ActivityBriefData(
                            activity.getActivityDate().toString(),
                            activity.getActivityCaption(),
                            activity.getActivityType(),
                            Integer.toString(activity.getActivityDuration()),
                            Double.toString(activity.getActivityDistance()),
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
