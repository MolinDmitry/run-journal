package ru.project.runjournal.run_journal;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief Предоставляет функции преобразования для формирования подробных сведений о тренировке
 */
public class ActivityDetailDataProcessor {

    public static String getActivityDateTimeAsString(Activities activity){
        return "дата и время";
    }

    public static String getActivityAveragePaceAsString(List<TrackPoints> trackPointsList){
        return "8\'57\"";
    }

    public static String getActivityAverageHrAsString(List<TrackPoints> trackPointsList){
        return "120";
    }

    public static String getActivityEnegryConsAsString(Activities activity){
        return "550";
    }

    public static String getActivityBestPaceAsString(List<TrackPoints> trackPointsList){
        return "5\'30\"";
    }

    public static String getActivityMaxHrAsString(List<TrackPoints> trackPointsList){
        return "155";
    }

    public static List<DistanceDetailsRow> getDistanceDetails(List<TrackPoints> trackPointsList){
        List<DistanceDetailsRow> result = new ArrayList<DistanceDetailsRow>();
        result.add(new DistanceDetailsRow((short)1 , "5\'30\"", (short)122, "5:30"));
        result.add(new DistanceDetailsRow((short)2 , "5\'45\"", (short)122, "1:15"));
        return result;
    }

}
