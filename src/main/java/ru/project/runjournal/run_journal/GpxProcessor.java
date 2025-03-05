package ru.project.runjournal.run_journal;

import org.springframework.web.multipart.MultipartFile;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
public class GpxProcessor {

    private List<TrackPoint> trkpntList;

    private class XMLHandler extends DefaultHandler{
        private String lastElementName;
        boolean trackPointFlag;
        boolean isFirstPtrackPoint;
        long curTrackId;
        TrackPoint curTrackPoint;

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
                curTrackPoint = new TrackPoint(
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude), 
                    LocalDateTime.of(1970,1,1,0,0), 
                    0, 0, (short)0,
                    0L
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
            information = information.replace("\n", "").trim();
            if (!information.isEmpty() && trackPointFlag){
                switch (lastElementName) {
                    case "time":
                        curTrackPoint.setTime(LocalDateTime.parse(information.replace("Z", "")));
                        break;
                    case "ns3:speed":
                        curTrackPoint.setSpeed(Float.parseFloat(information));
                        break;
                    case "ns3:cad":
                        curTrackPoint.setCadence(Float.parseFloat(information));
                        break;
                    case "ns3:hr":
                        curTrackPoint.setHr(Short.parseShort(information));
                        break;                
                    default:
                        break;
                }                    
            }
        }

    }

    public List<TrackPoint> processGPX(MultipartFile file){

            System.out.println(file.getName());
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getContentType());
            System.out.println(Long.toString(file.getSize()));

            trkpntList = new ArrayList<TrackPoint>();
            
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
                System.out.println(e.getMessage());
            }
            return trkpntList;        

    }

}
