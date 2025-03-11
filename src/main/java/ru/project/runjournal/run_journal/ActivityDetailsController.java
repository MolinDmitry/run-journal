package ru.project.runjournal.run_journal;

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

@Controller
@RequestMapping("/activitydetails")
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
                model.addAttribute("startDateTime", ActivityDetailDataProcessor.getActivityDateTimeAsString(curActivity));
                model.addAttribute("activityDistanceString", ActivityBriefDataProcessor.convertActivityDistanceToString(curActivity));
                model.addAttribute("activityDurationString", ActivityBriefDataProcessor.convertActivityDurationToString(curActivity.getActivityDuration(), true));
                int avgPace = ActivityDetailDataProcessor.getActivityAveragePace(curTrack);
                model.addAttribute("averagePaceString", ActivityDetailDataProcessor.convertPaceToString(avgPace));
                model.addAttribute("averageHrString", ActivityDetailDataProcessor.getActivityAverageHrAsString(curTrack));
                model.addAttribute("enegryConsString", ActivityDetailDataProcessor.getActivityEnegryConsAsString(curActivity,avgPace, 75)+" ккал");
                model.addAttribute("bestPaceString", ActivityDetailDataProcessor.convertPaceToString(ActivityDetailDataProcessor.getActivityBestPace(curTrack)));
                model.addAttribute("maxHrString", ActivityDetailDataProcessor.getActivityMaxHrAsString(curTrack));
                model.addAttribute("detailDistanceList",ActivityDetailDataProcessor.getDistanceDetails(curTrack));
            }
            else{
                return "redirect:errorpage?tracknotfounderror";
            }
            
            
        }
        else return "redirect:errorpage?activitynotfounderror";

        
        return "activitydetails";
    }

}
