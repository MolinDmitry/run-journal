package ru.project.runjournal.run_journal;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
/**
 * @brief Предоставляет набор статических методов для обработки данных тренировки
 */

public class ActivityDataProcessor {
    private final static double Rp = 6356.86; //полярный радиус Земли
    private final static double Re = 6378.2; //экваториальный радиус Земли

    public static  LocalDateTime getActivityStartTime(List<TrackPoints> trkPointsList){
        return trkPointsList.get(0).getTime();
    }

    public static  String getActivityCaption(List<TrackPoints> trkPointsList, String activityType){
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
        LocalDateTime activityStart = ActivityDataProcessor.getActivityStartTime(trkPointsList);
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

    public static int getActivityDuration(List<TrackPoints> trkPointsList){
        Long duration_sec = trkPointsList.get(trkPointsList.size()-1).getTime().toEpochSecond(ZoneOffset.UTC) - 
        trkPointsList.get(0).getTime().toEpochSecond(ZoneOffset.UTC);
        return duration_sec.intValue();
    }

    public static double getActivityDistance(List<TrackPoints> trkPointsList){
        double fi= trkPointsList.get(0).getLatitude()*Math.PI/180; // широта места тренировки в рад
        double R = Math.cos(fi)*(Re-Rp)+Rp; // радиус Земли на широте тренировки (при допущении формы Земли в виде идеального геоида)
        // аппроксимируем поверхность Земли сферой с радиусом R
        double Rm = R*Math.cos(fi); // радиус парралели в месте тренировки
        double sLat = 2*Math.PI*R/360; // длина дуги по поверхности на 1 градус широты
        double sLon = 2*Math.PI*Rm/360; // длина дуги по поверхности на 1 градус долготы
        double distance = 0;
        for(int i = 1; i<trkPointsList.size();i++){
            double dLat = (trkPointsList.get(i).getLatitude() - trkPointsList.get(i-1).getLatitude())*sLat;
            double dLon = (trkPointsList.get(i).getLongitude() - trkPointsList.get(i-1).getLongitude())*sLon;
            distance += Math.sqrt(dLat*dLat + dLon*dLon); 
        }

        return distance;
    }



}
