package ru.project.runjournal.run_journal;


import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ActivityDetailDataProcessorTests {


    @ParameterizedTest
    @CsvSource({
        "83,30;50;60;100;122",
        "130,0;120;130;140;220",
        "138,125;130;140;145;150",
        "--,0;0;0;0;0",
        "--,220;211;230;232;212"
    })
    void testGetActivityAverageHrAsString(String result, String inputData) {
        String [] strArr = inputData.split(";");
        List<TrackPoints> track = new ArrayList<TrackPoints>(); 
        for(int i =0; i<strArr.length; i++){
            TrackPoints trackPoint = mock();
            Mockito.when(trackPoint.getHr()).thenReturn(Short.valueOf(strArr[i]));
            track.add(trackPoint);
        }        
        Assertions.assertEquals(result, ActivityDetailDataProcessor.getActivityAverageHrAsString(track));
    }

    @ParameterizedTest
    @CsvSource({
        "5\'10\",310", 
        "4\'00\",240", 
        "6\'05\",365",
        "7\'30\",450",
        "0\'00\",0",
        "11\'10\",670",
        "100\'00\",6000"
    })
    void testGetActivityAveragePaceAsString(String result, int pace) {
        Assertions.assertEquals(result, ActivityDetailDataProcessor.convertPaceToString(pace));
    }


    @Test
    void testGetActivityAveragePace(){
        TrackPoints mockTrackPoint1 = Mockito.mock(TrackPoints.class);
        Mockito.when(mockTrackPoint1.getLatitude()).thenReturn(55.75);
        Mockito.when(mockTrackPoint1.getLongitude()).thenReturn(49.24);
        Mockito.when(mockTrackPoint1.getTime()).thenReturn(LocalDateTime.of(2025, 3, 5, 12, 0, 0));
                 
        TrackPoints mockTrackPoint2 = mock();
        Mockito.when(mockTrackPoint2.getLatitude()).thenReturn(55.749);
        Mockito.when(mockTrackPoint2.getLongitude()).thenReturn(49.239);
        Mockito.when(mockTrackPoint2.getTime()).thenReturn(LocalDateTime.of(2025, 3, 5, 12, 0, 30));

        TrackPoints mockTrackPoint3 = mock();
        Mockito.when(mockTrackPoint3.getLatitude()).thenReturn(55.748);
        Mockito.when(mockTrackPoint3.getLongitude()).thenReturn(49.24);
        Mockito.when(mockTrackPoint3.getTime()).thenReturn(LocalDateTime.of(2025, 3, 5, 12, 1, 30));

        List<TrackPoints> mockTrackPointsList = new ArrayList<TrackPoints>();
        mockTrackPointsList.add(mockTrackPoint1);
        mockTrackPointsList.add(mockTrackPoint2);
        mockTrackPointsList.add(mockTrackPoint3);

        Assertions.assertEquals(353, ActivityDetailDataProcessor.getActivityAveragePace(mockTrackPointsList));        
    }

    @ParameterizedTest
    @CsvSource({
        "360,33;330;39;390;36;360",
        "0,54;360",
        "0,63;330",
        "329,36;360;30;300;39;390"
    })
    void testGetActivityBestPace(int result, String inputData){
        // inputData - строка с парами значений интервал времени(сек);темп(сек\км)
        String[] strArray = inputData.split(";");
        List<TrackPoints> track = new ArrayList<>();
        double curLat = 55.75;
        double curLon = 49.24;
        double[] scaleFactors = ActivityDetailDataProcessor.getCoordinateScaleFactors(new TrackPoints(curLat, curLon));
        int numPoint = 0;
        for(int i=0;i<strArray.length;i+=2){
            double curPace = Math.sqrt(2)*Double.parseDouble(strArray[i+1]);
            double dLat = 1/(curPace*scaleFactors[0]);
            double dLon = 1/(curPace*scaleFactors[1]);
            for(int j=0;j<=Integer.parseInt(strArray[i]);j++)
            {
                int min = numPoint/60;
                int sec = numPoint%60;
                LocalDateTime time = LocalDateTime.of(2025, 3, 8, 12, min, sec);                
                TrackPoints point = mock();
                curLat+=dLat;
                curLon+=dLon;
                Mockito.when(point.getLatitude()).thenReturn(curLat);
                Mockito.when(point.getLongitude()).thenReturn(curLon);
                Mockito.when(point.getTime()).thenReturn(time);
                track.add(point);
                // добавляем ошибку в данные
                // в конец трека добавляем точку с другими координатами, но таким же временем, что у предыдущей
                // таким образом между последними двумя точками бесконечно большая скорость движения
                if (j==Integer.parseInt(strArray[i])&& i==(strArray.length-2)){
                    point = mock();
                    curLat+=dLat;
                    curLon+=dLon;
                    Mockito.when(point.getLatitude()).thenReturn(curLat);
                    Mockito.when(point.getLongitude()).thenReturn(curLon);
                    Mockito.when(point.getTime()).thenReturn(time);
                    track.add(point);
                    // также добавляем паузу
                    curLat+=dLat;
                    curLon+=dLon;
                    for(int q=1;q<=10;q++){
                        point = mock();
                        Mockito.when(point.getLatitude()).thenReturn(curLat);
                        Mockito.when(point.getLongitude()).thenReturn(curLon);
                        min = (numPoint+q)/60;
                        sec = (numPoint+q)%60;
                        time = LocalDateTime.of(2025, 3, 8, 12, min, sec);
                        Mockito.when(point.getTime()).thenReturn(time);
                        track.add(point);
                    }
                }
                numPoint++;                
            }
        }
        Assertions.assertEquals(result, ActivityDetailDataProcessor.getActivityBestPace(track));
        

    }

    @Test
    void testGetActivityDateTimeAsString() {
        
        for(int year = 2020; year<2035;year++){
            for(int month = 1; month <=12; month++){
                for(int day = 1; day<=28; day++){
                    for(int hour=0; hour<=23;hour++){
                        for(int min=0;min<60;min++){
                            String monthStr = "";
                            switch (month) {
                                case 1: monthStr = "янв"; break;
                                case 2: monthStr = "фев"; break;
                                case 3: monthStr = "мар"; break;
                                case 4: monthStr = "апр"; break;
                                case 5: monthStr = "май"; break;
                                case 6: monthStr = "июн"; break;
                                case 7: monthStr = "июл"; break;
                                case 8: monthStr = "авг"; break;
                                case 9: monthStr = "сен"; break;
                                case 10: monthStr = "окт"; break;
                                case 11: monthStr = "ноя"; break;
                                case 12: monthStr = "дек"; break;        
                                default: break;
                            }
                            LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, min, 0);
                            Activities testActivity =new Activities(
                                localDateTime,
                                null,
                                null,
                                null,
                                0,
                                0,
                                0L,
                                0L,
                                (byte)0,
                                (byte)0);
                            String result = Integer.toString(day) + "-" + monthStr + "-" + Integer.toString(year);
                            result += " " + Integer.toString(hour) + ":" + (min < 10 ? "0": "");
                            result += Integer.toString(min);
                            Assertions.assertEquals(result,ActivityDetailDataProcessor.getActivityDateTimeAsString(testActivity));                            
                        }
                    }
                }
            }
        }        
    }

    @ParameterizedTest
    @CsvSource({"3600,450,70", "5400,375,75", "1800,240,80"})
    void testGetActivityEnegryConsAsString(int duration, int pace, int m) {

        Activities testActivity =new Activities(
                                LocalDateTime.of(2025,2,5,0,10,0),
                                null,
                                null,
                                null,
                                duration,
                                0,
                                0L,
                                0L,
                                (byte)0,
                                (byte)0);
        String result = String.format("%.0f", 61.25*60/pace*m*duration/3600);
        
        Assertions.assertEquals(result, ActivityDetailDataProcessor.getActivityEnegryConsAsString(testActivity, pace, m));

    }

    @ParameterizedTest
    @CsvSource({
        "118,30;50;60;100;122;130;131;132;134;135;0;115;120",
        "--,0;120;130;140;220",
        "--,125;130;140;145;150",
        "--,0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0",
        "--,220;211;230;232;212;220;211;230;232;212;220;211;230;232;212"
    })
    void testGetActivityMaxHrAsString(String result, String inputData){
        String[] strArray = inputData.split(";");
        List<TrackPoints> track = new ArrayList<>();
        for(int i =0; i<strArray.length;i++){
            TrackPoints point = mock();
            Mockito.when(point.getHr()).thenReturn(Short.valueOf(strArray[i]));
            track.add(point);
        }
        Assertions.assertEquals(result, ActivityDetailDataProcessor.getActivityMaxHrAsString(track));
    }

    @Test
    void testGetDistanceDetails(){
        double trackDist = 3.4;
        List<DistanceDetailsRow> expectedResult = Arrays.asList(
            new DistanceDetailsRow(1,"5\'30\"",(short)150,"0:05:31"),
            new DistanceDetailsRow(2,"4\'59\"",(short)160,"0:10:30"),
            new DistanceDetailsRow(3,"6\'00\"",(short)140,"0:16:31"),
            new DistanceDetailsRow(trackDist,"5\'57\"",(short)140,"0:18:53")
            );
        List<TrackPoints> track = new ArrayList<>();
        double dist = 0;
        double curLat = 55.75;
        double curLon = 49.24;
        short curHR = 150;
        double[] scaleFactors = ActivityDetailDataProcessor.getCoordinateScaleFactors(new TrackPoints(curLat, curLon));
        int numPoint = 0;
        int curTotalPace = 330;        
        TrackPoints point;
        while(dist<trackDist){  
            double curPace = Math.sqrt(2)*curTotalPace;
            double dLat = 1/(curPace*scaleFactors[0]);
            double dLon = 1/(curPace*scaleFactors[1]);
            point = mock();
            int min = numPoint/60;
            int sec = numPoint%60;
            LocalDateTime time = LocalDateTime.of(2025,3,10, 12,min, sec);
            Mockito.when(point.getLatitude()).thenReturn(curLat);
            Mockito.when(point.getLongitude()).thenReturn(curLon);
            Mockito.when(point.getTime()).thenReturn(time);
            Mockito.lenient().when(point.getHr()).thenReturn(curHR); // ругается на ненужный mock, lenient() для игнорирования ошибки
            track.add(point);
            numPoint++;
            curLat+=dLat;
            curLon+=dLon;
            dist+= 1.0/curTotalPace;
            if (dist > 1){
                curTotalPace = 300;
                curHR = 160;
            }
            if (dist > 2){
                curTotalPace = 360;
                curHR = 140;
            }            
        }

        List<DistanceDetailsRow> obtainedResult = ActivityDetailDataProcessor.getDistanceDetails(track);
        for (int i = 0; i < expectedResult.size(); i++) {
            Assertions.assertEquals(expectedResult.get(i).getMilestone(), obtainedResult.get(i).getMilestone());
            Assertions.assertEquals(expectedResult.get(i).getPace(), obtainedResult.get(i).getPace());
            Assertions.assertEquals(expectedResult.get(i).getHr(), obtainedResult.get(i).getHr());
            Assertions.assertEquals(expectedResult.get(i).getTimeString(), obtainedResult.get(i).getTimeString());            
        }
    }
}
