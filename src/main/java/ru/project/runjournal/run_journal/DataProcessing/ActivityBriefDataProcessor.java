package ru.project.runjournal.run_journal.DataProcessing;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

import ru.project.runjournal.run_journal.Entities.Activities;

/**
 * @brief Обрабатывает краткие данные о тренировке для журнала тренировок
 */

public class ActivityBriefDataProcessor {

/**
 * @brief Ковертирует дату и время из UTC в локальное время часового пояса
 * @param UTCdateTime время UTC
 * @param ZoneOffsetHour часы смещения временной зоны
 * @param ZoneOffsetMin минуты смещения временной зоны
 * @return
 */
    public static LocalDateTime convertToLocalDateTime(LocalDateTime UTCdateTime, byte ZoneOffsetHour, byte ZoneOffsetMin){
        LocalDateTime dt_UTC = UTCdateTime;
        long epoch_sec = dt_UTC.toEpochSecond(ZoneOffset.UTC);
        LocalDateTime dt_local = LocalDateTime.ofEpochSecond(epoch_sec, 0, ZoneOffset.ofHoursMinutes(ZoneOffsetHour, ZoneOffsetMin));
        return dt_local;
    }
   
/**
 * @brief Конвертирует дату тренировки в формат день-месяц-год в локальном часовом поясе
 * @return Строка с датой тренировки
 */
    public static String convertActivityDateToString(Activities activity){
        LocalDateTime activityDate = activity.getActivityDate();
        byte zoneOffsetH = activity.getTimeZoneOffsetHours();
        byte ZoneOffsetM = activity.getTimeZoneOffsetMinuts();
        LocalDateTime activityLocalDateTime = ActivityBriefDataProcessor.convertToLocalDateTime(activityDate, zoneOffsetH, ZoneOffsetM);
        int year = activityLocalDateTime.getYear();
        Month month = activityLocalDateTime.getMonth();
        int day = activityLocalDateTime.getDayOfMonth();
        String result = Integer.toString(day);
        switch (month) {
            case Month.JANUARY:
                result += "-янв-";
                break;
            case Month.FEBRUARY:
                result += "-фев-";
                break;
            case Month.MARCH:
                result += "-мар-";
                break;
            case Month.APRIL:
                result += "-апр-";
                break;
            case Month.MAY:
                result += "-май-";
                break;
            case Month.JUNE:
                result += "-июн-";
                break;
            case Month.JULY:
                result += "-июл-";
                break;
            case Month.AUGUST:
                result += "-авг-";
                break;
            case Month.SEPTEMBER:
                result += "-сен-";
                break;
            case Month.OCTOBER:
                result += "-окт-";
                break;
            case Month.NOVEMBER:
                result += "-ноя-";
                break;
            case Month.DECEMBER:
                result += "-дек-";
                break;
            default:
                result += "-   -";    
                break;
        }
        result += Integer.toString(year);
        return result;
    }
    /**
     * @brief Конвертирует длительность тренировки в строку формата 1 ч 10 мин или 1:10:20 
     * @param activity Сущность тренировки
     * @param withSecondsFlag флаг выбора формата 
     * @return Строка с длительностью тренировки
     */
    public static String convertActivityDurationToString(int duration, boolean withSecondsFlag){
        Integer durH = duration/3600;
        Integer durM = duration/60 - durH*60;
        String result = durH.toString();
        if (withSecondsFlag){
            Integer durS = duration%60;
            result = result + ":" + (durM >=10 ? durM.toString() : "0"+ durM.toString());
            result = result + ":" + (durS >=10 ? durS.toString() : "0"+ durS.toString());            
        }
        else
        {
            result = result + "ч " + (durM >10 ? durM.toString() : "0"+ durM.toString());
            result += "мин";
        }        
        return  result;
    }
    /**
     * @brief Конвертирует дистанцию тренировки в стоку с округлением до двух знаков после запятой
     * @param activity Сущность тренировки
     * @return Строка с дистанцией тренировки
     */
    public static String convertActivityDistanceToString(Activities activity){
        return String.format("%.2f", activity.getActivityDistance()).replace(",", ".");
    }

    /**
     * @brief Конвертирует тип тренировки в русскоязычное нименование
     * @return Строку с русским наименованием типа тренировки
     */
    public static String convertActivityTypeToRusString(Activities activity){
        String result = "";
        switch (activity.getActivityType()) {
            case "RUNNING":
                result = "Бег";
                break;
            case "RACE":
                result = "Бег";
                break;
            case "TRAILRUNNING":
                result = "Бег";
                break;
            case "HIKING":
                result = "Ходьба";
                break;
            case "TREKKING":
                result = "Поход";
                break;
            case "WALK":
                result = "Прогулка";
                break;
            case "BIKETOUR":
                result = "Велосипед";
                break;
            case "BIKERIDE":
                result = "Велосипед";
                break;
            case "BIKERACE":
                result = "Велосипед";
                break;
            case "NORDICSKI":
                result = "Лыжи";
                break;
            case "CLASSICSKI":
                result = "Лыжи";
                break;
            case "SKATESKI":
                result = "Лыжи";
                break;        
            default:
                result = "Тренировка";
                break;
        }
        return result;
    }



}
