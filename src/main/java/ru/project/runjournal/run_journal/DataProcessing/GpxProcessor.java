package ru.project.runjournal.run_journal.DataProcessing;

import org.springframework.web.multipart.MultipartFile;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lombok.extern.slf4j.Slf4j;
import ru.project.runjournal.run_journal.Entities.TrackPoints;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @breif Обрабатывает файл формата GPX 
 */
@Slf4j
public class GpxProcessor {

    private List<TrackPoints> trkpntList;

    private class XMLHandler extends DefaultHandler{
        private String lastElementName;
        boolean trackPointFlag;
        boolean isFirstPtrackPoint;
        long curTrackId;
        TrackPoints curTrackPoint;

        public void setFirstTrackPointFlag(){
            this.isFirstPtrackPoint = true;
        }
        

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
            lastElementName = qName;
            
            if (qName.equals("trkpt")){
                trackPointFlag = true;
                String latitude = attributes.getValue("lat");
                String longitude = attributes.getValue("lon");
                curTrackPoint = new TrackPoints(
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude)
                    );
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException{
            if (qName.equals("trkpt")) {
                trackPointFlag=false;
                if (isFirstPtrackPoint){
                    isFirstPtrackPoint = false;
                    curTrackId = curTrackPoint.getTime().toEpochSecond(ZoneOffset.UTC);
                }
                curTrackPoint.setTrackId(curTrackId);
                trkpntList.add(curTrackPoint);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException{
            String information = new String(ch,start,length);
            information = information.replace("\n", "").replace(",", ".")
            .replace(" ", "").trim();
            if (!information.isEmpty() && trackPointFlag){
                switch (lastElementName) {
                    case "time":
                        curTrackPoint.setTime(LocalDateTime.parse(information.replace("Z", "")));
                        break;
                    case "ns3:speed":
                        try{
                            curTrackPoint.setSpeed(Float.parseFloat(information));
                        }catch(Exception e){
                            log.info("Error during speed parsing. Speed is set to 0");
                            curTrackPoint.setSpeed(0);
                        }                        
                        break;
                    case "ns3:cad":
                        try{
                            curTrackPoint.setCadence(Float.parseFloat(information));
                        }catch(Exception e){
                            log.info("Error during cadence parsing. Cadence is set to 0");
                            curTrackPoint.setCadence(0);
                        }                        
                        break;
                    case "ns3:hr":
                        try {
                            curTrackPoint.setHr(Short.parseShort(information));                        
                        } catch (Exception e) {
                            log.info("Error during HR parsing. HR is set to 0");
                            curTrackPoint.setHr((short)0);
                        }                        
                        break;                
                    default:
                        break;
                }                    
            }
        }

    }
    /**
     * @brief Обрабатывает файл формата GPX
     * @param file Файл формата GPX
     * @return Список точек трека в виде объектов TrackPoint c id трека. 
     * При возникновении ошибок в процессе обработки возвращает пустой список
     */
    public List<TrackPoints> processGPX(MultipartFile file){

            trkpntList = new ArrayList<TrackPoints>();
            
            try{
                InputStream is = file.getInputStream();
                // парсинг XML с помощью SAX
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLHandler handler = new XMLHandler();
                handler.setFirstTrackPointFlag();
                parser.parse(is,handler);               

            }
            catch(Exception e){
                trkpntList = null;
                log.error("Error XML parsing", e);
            }
            return trkpntList;        

    }

}
