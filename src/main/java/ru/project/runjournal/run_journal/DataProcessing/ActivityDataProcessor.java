package ru.project.runjournal.run_journal.DataProcessing;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import ru.project.runjournal.run_journal.Entities.Activities;
import ru.project.runjournal.run_journal.Entities.DistanceDetailsRow;
import ru.project.runjournal.run_journal.Entities.TrackPoints;

/**
 * @brief Библиотека статических методов для обработки данных тренировки
 */

public class ActivityDataProcessor {
    private final static double Rp = 6356.86; //полярный радиус Земли
    private final static double Re = 6378.2; //экваториальный радиус Земли
    
    public static LocalDateTime getActivityStartTime(List<TrackPoints> trkPointsList, byte ZoneOffsetHour, byte ZoneOffsetMin){
        LocalDateTime dt_UTC = trkPointsList.get(0).getTime();
        return convertToLocalDateTime(dt_UTC, ZoneOffsetHour, ZoneOffsetMin);
    }

    public static String getActivityCaption(String activityType, List<TrackPoints> trkPointsList, byte ZoneOffsetHour, byte ZoneOffsetMin){
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
        LocalDateTime activityStart = getActivityStartTime(trkPointsList, ZoneOffsetHour,ZoneOffsetMin);
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

    /**
     * @brief Возвращает дистанцию тренировки
     * Участки с темпом вне диапазона 20-800 сек/км не учитываются 
     * @return Значение дистанции в км
     */
    public static double getActivityDistance(List<TrackPoints> trkPointsList){
        double distance = 0;
        double[] coordScales = getCoordinateScaleFactors(trkPointsList.get(0));
        for(int i = 1; i<trkPointsList.size();i++){
            double dl= getDistanceBetweenTrackPoints(
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
        LocalDateTime activityLocalDateTime = convertToLocalDateTime(activityDate, zoneOffsetH, ZoneOffsetM);
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


    /**
     * @brief Возвращает дату и время начала тренировки в виде строки вида 5-мар-2025 13:58
     * @param activity Сущность тренировки
     * @return Строка с датой и временем начала тренировки
     */
    public static String getActivityDateTimeAsString(Activities activity){
        LocalDateTime localDateTime = convertToLocalDateTime(
            activity.getActivityDate(), 
            activity.getTimeZoneOffsetHours(), 
            activity.getTimeZoneOffsetMinuts());
        String dateString = convertActivityDateToString(activity);
        int startTimeH = localDateTime.getHour();
        int startTimeM = localDateTime.getMinute();
        // В целях исключения дублирования кода, поместим время начала тренировки в объект Activities в поле длительности
        Activities tempActivity = new Activities(
            localDateTime, "", "", null, 
            startTimeH*3600+startTimeM*60, 0, 0, 0, (byte)0, (byte)0);
        // получим строку времени начала в виде час:мин:сек
        String timeString = convertActivityDurationToString(tempActivity.getActivityDuration(), true);
        String[] stringArr = timeString.split(":");
        String startTimeString = "--";
        if (stringArr.length >= 2){
            startTimeString = stringArr[0] + ":" + stringArr[1];
        }        
        return dateString+" "+startTimeString;
    }

    /**
     * @brief Конвертирует значение темпа в строку
     * @param pace Темп в секундах/км
     * @return Строка вида мин'сек"
     */
    public static String convertPaceToString(int pace){
        int paceM = pace/60;
        int paceS = pace%60;
        String result = Integer.toString(paceM) + "\'";
        result += paceS >= 10 ? Integer.toString(paceS): "0"+Integer.toString(paceS);
        result += "\"";
        return  result;
    }

    /**
     * @brief Возвращает средний темп тренировки без учета пауз в движении
     * Паузой считается движение с темпом более 12 мин/км
     * @param trackPointsList Список точек трека
     * @return Значение темпа в секундах/км
     */
    public static int getActivityAveragePace(List<TrackPoints> trackPointsList){
        double distance = 0;
        long timeMilli = 0L;
        double[] coordScales = getCoordinateScaleFactors(trackPointsList.get(0));
        for(int i =1; i<trackPointsList.size();i++){
            double dl = getDistanceBetweenTrackPoints(
                trackPointsList.get(i), 
                trackPointsList.get(i-1), 
                coordScales);
            long dt =  trackPointsList.get(i).getTime().toInstant(ZoneOffset.UTC).toEpochMilli() - 
                        trackPointsList.get(i-1).getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            double localPace = 200000;
            if (dl>0.000001) localPace = dt/(1000*dl);
            if (localPace> 20 && localPace < 800){
                distance+=dl;
                timeMilli+=dt;                
            }
        }
        double pace = (double)(timeMilli)/(distance*1000);
        return (int)Math.round(pace);
    }

    /**
     * @brief Возвращает средний пульс в виде строки без учета пульса вне диапазона 40-210
     * @param trackPointsList Список точек трека
     * @return Средний пульс уд/мин
     */
    public static String getActivityAverageHrAsString(List<TrackPoints> trackPointsList){
        int[] averageHR = new int[1];
        int[] counter = new int[1];
        averageHR[0] = 0;
        counter[0] = 0;
        trackPointsList.stream().forEach(
            trackPoint ->{
                int curHR = trackPoint.getHr();
                if (curHR > 40 && curHR < 210){
                    averageHR[0]+=curHR;
                    counter[0]++;
                }
            }
        );
        if (counter[0]!=0){
            averageHR[0] = averageHR[0]/counter[0];
        }
        else return "--";
        try{
            return Integer.toString(averageHR[0]);
        }
        catch(Exception e){
            return "--";
        }        
    }

    /**
     * @brief Возвращает максимальный пульс за тренировку
     * Усреднение 10 точек трека без учета данных вне диапазона 40-210
     * Если точек меньше 10, то пульс не рассчитывается
     * @param trackPointsList Список точек трека
     * @return Максимальный пульс уд/мин
     */
    public static String getActivityMaxHrAsString(List<TrackPoints> trackPointsList){
        int counter = 0;
        int maxHr = 0;
        final int filterWindowWidth = 10;
        int[] slidingWindow = new int[filterWindowWidth];
        boolean tenPointsIsPresentFlag = false;
        for(var point:trackPointsList){
            if (point.getHr()>=40 && point.getHr()<=210){
                slidingWindow[counter] = point.getHr();
                counter++;
                if(counter==filterWindowWidth){
                    counter=0;
                    if (!tenPointsIsPresentFlag) tenPointsIsPresentFlag=true;
                }
                if (tenPointsIsPresentFlag){
                    int curFilteredHR = 0;
                    for(int j = 0;j<filterWindowWidth;j++){
                        curFilteredHR +=slidingWindow[j];
                    }
                    curFilteredHR = (int)Math.round(((double)(curFilteredHR)/filterWindowWidth));
                    if (curFilteredHR >maxHr) maxHr = curFilteredHR;  
                }
                                              
            }            
        }
        if (maxHr != 0) return Integer.toString(maxHr);
        else return "--";
    }

    /**
     * @brief Возвращает количество потраченых ккал на тренировку
     * @param activity Сущность тренировки
     * @param averagePace Средний темп в сек/км
     * @param mass Масса спортсмена в кш
     * @return Строка со значением сожженных ккал
     */
    public static String getActivityEnegryConsAsString(Activities activity, int averagePace, int mass){
        int activityDuration = activity.getActivityDuration();
        //  коэффициент 61.25 зависит от вида активности (MET)       
        return String.format("%.0f", 61.25*60/averagePace*mass*activityDuration/3600);
    }

    /**
     * @brief Возвращает значение лучшего темпа на км 
     * Анализируются отрезки в 200 м
     * Если тренировка менее 200 м, возвращает 0
     * Темп вне диапазона 20-800 сек\км не учитывается
     * @param trackPointsList Список точек трека
     * @return Значение темпа в сек\км
     */
    public static int getActivityBestPace(List<TrackPoints> trackPointsList){
        double distance = 0;
        double time = 0;
        double[] scaleFactors = getCoordinateScaleFactors(trackPointsList.get(0));
        List<double[]> timeDistance = new ArrayList<>();
        double[] pair0 = {0,0};
        timeDistance.add(pair0);
        for(int i=1; i<trackPointsList.size();i++){
            double dl = getDistanceBetweenTrackPoints(
                trackPointsList.get(i),
                trackPointsList.get(i-1),
                scaleFactors);
            long t0 = trackPointsList.get(i-1).getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            long t1 = trackPointsList.get(i).getTime().toInstant(ZoneOffset.UTC).toEpochMilli();            
            if (Math.abs(dl)>0.00001){
                double curPace = (double)(t1-t0)/(1000*dl);
                if (curPace>=20 && curPace <=800){
                    distance+=dl;
                    time+=(double)(t1-t0)/1000;
                    double[] pair = {time,distance};
                    timeDistance.add(pair);
                }
            }
        }
        double ms0 = 0;
        double ms1 = 0;
        int ind0 = 0;
        double bestPace = 20000;
        for (int i = 0; i < timeDistance.size(); i++){
            ms1 = timeDistance.get(i)[1];
            double curSection = ms1-ms0;
            if ((curSection)>=0.2){
                double curPace = (timeDistance.get(i)[0]-timeDistance.get(ind0)[0])/curSection;
                if (curPace<bestPace) bestPace = curPace;
                while((ms1 - ms0)>=0.2){
                    ind0++;
                    ms0 = timeDistance.get(ind0)[1];
                }
            }           
        }
        if (bestPace > 800 || bestPace < 20) bestPace = 0;
        return (int)Math.round(bestPace);
    }

    

    /**
     * @brief Возвращает сведения по км
     * @param trackPointsList Список точек трека
     * Если последний неполный километр меньше 0.2, то данные не включаются
     * При пульс вне диапазона 40-210 не учитывается
     * Темп вне диапазона 20-800 сек\км не учитывается
     * @return Данные о темпе пульсе в времени преодоления километровых отметок
     */
    public static List<DistanceDetailsRow> getDistanceDetails(List<TrackPoints> trackPointsList){
        List<DistanceDetailsRow> result = new ArrayList<DistanceDetailsRow>();
        double[] scaleFactors = getCoordinateScaleFactors(trackPointsList.get(0));
        double sector = 0;
        double time = 0;
        double sectorStartTime = -1;
        int numSector = 1;
        int numHrSamples = 0;
        int sumHR = 0;
        for (int i = 1; i < trackPointsList.size(); i++){
            double dl = getDistanceBetweenTrackPoints(
                trackPointsList.get(i),
                trackPointsList.get(i-1), 
                scaleFactors);
            long t0 = trackPointsList.get(i-1).getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            long t1 = trackPointsList.get(i).getTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            double curPace = 20000;
            if (dl>0.000001) curPace = ((double)(t1-t0))/(1000*dl);
            short curHR = trackPointsList.get(i).getHr();
            if (curHR >= 40 && curHR <=210){
                sumHR+=curHR;
                numHrSamples++;
            }
            System.out.println(curPace);
            if (curPace>20 && curPace<800){
                if (sectorStartTime < 0) sectorStartTime = time;
                sector+=dl;
                time+=((double)(t1-t0))/1000;                                
            }
            if (sector > 1|| (i==(trackPointsList.size()-1) && sector >= 0.2)){
                int sectorPace = (int)Math.round((double)(time-sectorStartTime)/sector);
                result.add(new DistanceDetailsRow(
                    sector>1 ? (double)numSector: ((double)Math.round((sector+numSector-1)*10))/10,
                    convertPaceToString(sectorPace),
                    (short)Math.round((double)sumHR/numHrSamples), 
                    convertActivityDurationToString((int)Math.round(time), true)));
                sector-=1;
                sectorStartTime = time;
                sumHR=0;
                numHrSamples = 0;
                numSector++;
                
            }            
        }
        return result;
    }

    /**
     * @brief Возвращает коэффициенты расстояния на градус широты и долготы
     * @param trackPoint Точка трека, для которой производится расчет коэффициентов
     * @return Массив значений коэффициентов по широте и долготе
     */
    public static double[] getCoordinateScaleFactors(TrackPoints trackPoint){
        double fi= trackPoint.getLatitude()*Math.PI/180; // широта места тренировки в рад
        double R = Math.cos(fi)*(Re-Rp)+Rp; // радиус Земли на широте тренировки (при допущении формы Земли в виде идеального геоида)
        // аппроксимируем поверхность Земли сферой с радиусом R
        double Rm = R*Math.cos(fi); // радиус параллели в месте тренировки
        double sLat = 2*Math.PI*R/360; // длина дуги по поверхности на 1 градус широты
        double sLon = 2*Math.PI*Rm/360; // ((double)(t1-t0))/(1000*dl)длина дуги по поверхности на 1 градус долготы 
        double[] retVal={sLat,sLon};
        return retVal;
    }

    /**
     * @brief Возвращает расстояние между двумя соседними точками трека
     * @param trackPoint Точка трека
     * @param lastTrackPoint Предыдущая точка трека
     * @param coordScaleFactors массив коэффициентов расстояния на грудус по широте и долготе
     * @return значение расстояния между двумя соседними точками трека
     */
    public static double getDistanceBetweenTrackPoints(TrackPoints trackPoint, TrackPoints lastTrackPoint, double[] coordScaleFactors){          
        double dLat = (trackPoint.getLatitude() - lastTrackPoint.getLatitude())*coordScaleFactors[0];
        double dLon = (trackPoint.getLongitude() - lastTrackPoint.getLongitude())*coordScaleFactors[1];
        return Math.sqrt(dLat*dLat + dLon*dLon);
    }


}
