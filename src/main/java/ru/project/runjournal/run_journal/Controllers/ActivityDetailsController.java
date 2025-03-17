package ru.project.runjournal.run_journal.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.project.runjournal.run_journal.DataProcessing.ActivityDataProcessor;
import ru.project.runjournal.run_journal.Entities.Activities;
import ru.project.runjournal.run_journal.Entities.TrackPoints;
import ru.project.runjournal.run_journal.Entities.Users;
import ru.project.runjournal.run_journal.Repositories.ActivitiesRepository;
import ru.project.runjournal.run_journal.Repositories.TrackPointsRepository;

@Controller
@RequestMapping("/run-journal/activitydetails")
public class ActivityDetailsController {

    private final ActivitiesRepository activityRepo;
    private final TrackPointsRepository trackPointsRepo;

    @Autowired
    public ActivityDetailsController(ActivitiesRepository activityRepository, TrackPointsRepository trackPointsRepository){
        this.activityRepo = activityRepository;
        this.trackPointsRepo = trackPointsRepository;
    }


    @ModelAttribute
    public void createModelAtrributes(@AuthenticationPrincipal Users curUser, Model model){
        model.addAttribute("currentUsername", curUser.getUsername());
        model.addAttribute("currentFirstName", curUser.getFirstName());
        model.addAttribute("currentLastName", curUser.getLastName());
    }



    @GetMapping
    public String showActivityDetailsPage(@RequestParam long id, Model model){        
        Optional<Activities> curActivityOptional= activityRepo.findById(id);
        if (curActivityOptional.isPresent()){
            Activities curActivity = curActivityOptional.get();
            List<TrackPoints> curTrack= trackPointsRepo.findByTrackIdOrderByTime(curActivity.getTrackId());
            if (!curTrack.isEmpty()){
                model.addAttribute("startDateTime", ActivityDataProcessor.getActivityDateTimeAsString(curActivity));
                model.addAttribute("activityDistanceString", ActivityDataProcessor.convertActivityDistanceToString(curActivity));
                model.addAttribute("activityDurationString", ActivityDataProcessor.convertActivityDurationToString(curActivity.getActivityDuration(), true));
                int avgPace = ActivityDataProcessor.getActivityAveragePace(curTrack);
                model.addAttribute("averagePaceString", ActivityDataProcessor.convertPaceToString(avgPace));
                model.addAttribute("averageHrString", ActivityDataProcessor.getActivityAverageHrAsString(curTrack));
                model.addAttribute("enegryConsString", ActivityDataProcessor.getActivityEnegryConsAsString(curActivity,avgPace, 75)+" ккал");
                model.addAttribute("bestPaceString", ActivityDataProcessor.convertPaceToString(ActivityDataProcessor.getActivityBestPace(curTrack)));
                model.addAttribute("maxHrString", ActivityDataProcessor.getActivityMaxHrAsString(curTrack));
                model.addAttribute("detailDistanceList",ActivityDataProcessor.getDistanceDetails(curTrack));
            }
            else{
                return "redirect:run-journal/errorpage?tracknotfounderror";
            }
            
            
        }
        else return "redirect:run-journal/errorpage?activitynotfounderror";

        
        return "activitydetails";
    }

}
