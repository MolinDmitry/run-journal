package ru.project.runjournal.run_journal.Controllers;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import lombok.extern.slf4j.Slf4j;
import ru.project.runjournal.run_journal.DataProcessing.ActivityDataProcessor;
import ru.project.runjournal.run_journal.DataProcessing.GpxProcessor;
import ru.project.runjournal.run_journal.Entities.Activities;
import ru.project.runjournal.run_journal.Entities.TrackPoints;
import ru.project.runjournal.run_journal.Entities.Users;
import ru.project.runjournal.run_journal.Repositories.ActivitiesRepository;
import ru.project.runjournal.run_journal.Repositories.TrackPointsRepository;


@Slf4j
@Controller
@RequestMapping("/run-journal/addactivity")
public class ActivityAddingController {

    private final ActivitiesRepository activitiesRepo;
    private final TrackPointsRepository trackPointsRepo;

    @Autowired
    public ActivityAddingController(ActivitiesRepository activitiesRepository, TrackPointsRepository trackPointsRepository){
        this.activitiesRepo = activitiesRepository;
        this.trackPointsRepo = trackPointsRepository;
    }

    @Data
    @AllArgsConstructor
    private class ActivityInitialData{
        private String activityType;
        private List<String> activityComment;
        private MultipartFile fileGPX;
    }

    private enum ActivityType {
        RUNNING,
        RACE,
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
     * 
     * - Принимает первоначальные данные о тренировке и файл формата GPX. 
     * - Сохраняет данные о тренировке в БД в таблицы activities и trackpoints
     * - Каждая запись в activities содержит id трека
     *   
     * 
     * @return Возвращает перенаправление на страницу журнала тренировок
     */
    @PostMapping
    public String processActivityAdding(@AuthenticationPrincipal Users currentUser, ActivityInitialData activityData){
        if (!activityData.fileGPX.isEmpty()){
                List<TrackPoints> trackPointsList = new GpxProcessor().processGPX(activityData.fileGPX);
                if (trackPointsList != null){
                    // формируем данные для тренировки для сохранения в БД
                    // ActivityInitialDataProcessor dataProc = new ActivityInitialDataProcessor(trackPointsList, activityData.getActivityType());
                    Activities curActivity = new Activities(
                        ActivityDataProcessor.getActivityStartTime(trackPointsList,(byte)0,(byte)0),
                        activityData.getActivityType(),
                        ActivityDataProcessor.getActivityCaption(activityData.getActivityType(),trackPointsList,(byte)3,(byte)0),
                        activityData.getActivityComment(),
                        ActivityDataProcessor.getActivityDuration(trackPointsList),
                        ActivityDataProcessor.getActivityDistance(trackPointsList),
                        trackPointsList.get(0).getTrackId(),
                        currentUser.getId(),
                        (byte)3,
                        (byte)0
                    );
                    List<Activities> existingActivities = activitiesRepo.findByTrackIdAndUserId(trackPointsList.get(0).getTrackId(), currentUser.getId());
                    if (existingActivities.isEmpty()){
                        activitiesRepo.save(curActivity);
                        trackPointsRepo.saveAll(trackPointsList);
                    }
                    else
                    {
                        return "redirect:run-journal/addactivity?existingactivityerror";
                    }
                }
                else{
                    log.info("Null trackPointsList is obtained after processing");
                    return "redirect:run-journal/addactivity?gpxprocessingerror";
                }                
            }
        else{
            return "redirect:run-journal/addactivity?gpxisnot";
        }
        return "redirect:/run-journal";
    }
}
