package ru.project.runjournal.run_journal.DataProcessing;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import ru.project.runjournal.run_journal.Entities.TrackPoints;
/**
 * @brief Обрабатывает данные тренировки
 */

public class ActivityInitialDataProcessor {
    private List<TrackPoints> trkPointsList;
    private String activityType;

    public ActivityInitialDataProcessor(List<TrackPoints> trkPointsList, String activityType){
        this.trkPointsList = trkPointsList;
        this.activityType = activityType;
    }

    public LocalDateTime getActivityStartTime(byte ZoneOffsetHour, byte ZoneOffsetMin){
        LocalDateTime dt_UTC = trkPointsList.get(0).getTime();
        return ActivityBriefDataProcessor.convertToLocalDateTime(dt_UTC, ZoneOffsetHour, ZoneOffsetMin);
    }

    public String getActivityCaption(byte ZoneOffsetHour, byte ZoneOffsetMin){
        boolean mIndicator; // true - мужской род
        String typeRusString = "";
        String timeOfDayString = "";
        switch (activityType) {
            case "RUNNING":
                mIndicator = false;
                typeRusString = "пробежка";
                break;
            case "RACE":
                mIndicator = true;
                typeRusString = "забег";
                break;
            case "TRAILRUNNING":
                mIndicator = true;
                typeRusString = "кросс";
                break;
            case "HIKING":
                mIndicator = true;
                typeRusString = "поход";
                break;
            case "TREKKING":
                mIndicator = true;
                typeRusString = "многодневный поход";
                break;
            case "WALK":
                mIndicator = false;
                typeRusString = "прогулка";
                break;
            case "BIKETOUR":
                mIndicator = true;
                typeRusString = "велопоход";
                break;
            case "BIKERIDE":
                mIndicator = false;
                typeRusString = "велопрогулка";
                break;
            case "BIKERACE":
                mIndicator = false;
                typeRusString = "велогонка";
                break;
            case "NORDICSKI":
                mIndicator = false;
                typeRusString = "лыжная прогулка";
                break;
            case "CLASSICSKI":
                mIndicator = false;
                typeRusString = "лыжная пробежка (классический стиль)";
                break;
            case "SKATESKI":
                mIndicator = false;
                typeRusString = "лыжная пробежка (свободный стиль)";
                break;        
            default:
                mIndicator = false;
                typeRusString = "тренировка";
                break;
        }
        LocalDateTime activityStart = this.getActivityStartTime(ZoneOffsetHour,ZoneOffsetMin);
        int activityHour = activityStart.getHour();
        if (activityHour >=4 && activityHour < 11)
            timeOfDayString = mIndicator ? "Утренний":"Утренняя";
        else
            if (activityHour >=11 && activityHour < 17)
                timeOfDayString = mIndicator ? "Дневной":"Дневная";
            else
                if (activityHour >=17 && activityHour < 22)
                    timeOfDayString = mIndicator ? "Вечерний":"Вечерняя";
                else
                    timeOfDayString = mIndicator ? "Ночной":"Ночная";

        return timeOfDayString + " " + typeRusString;
    }

    public int getActivityDuration(){
        Long duration_sec = trkPointsList.get(trkPointsList.size()-1).getTime().toEpochSecond(ZoneOffset.UTC) - 
        trkPointsList.get(0).getTime().toEpochSecond(ZoneOffset.UTC);
        return duration_sec.intValue();
    }

    /**
     * @brief Возвращает дистанцию тренировки
     * Участки с темпом вне диапазона 20-800 сек/км не учитываются 
     * @return Значение дистанции в км
     */
    public double getActivityDistance(){
        double distance = 0;
        double[] coordScales = ActivityDetailDataProcessor.getCoordinateScaleFactors(trkPointsList.get(0));
        for(int i = 1; i<trkPointsList.size();i++){
            double dl= ActivityDetailDataProcessor.getDistanceBetweenTrackPoints(
                trkPointsList.get(i),
                trkPointsList.get(i-1),
                coordScales
                );
            long t0 = trkPointsList.get(i-1).getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            long t1 = trkPointsList.get(i).getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            double pace = 20000;
            if (dl>0.00001) pace = ((double)(t1-t0))/(1000*dl);
            if (pace > 20 && pace < 800){
                distance+=dl;
            }           

        }
        return distance;
    }



}
