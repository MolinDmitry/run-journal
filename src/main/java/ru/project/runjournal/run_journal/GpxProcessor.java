package ru.project.runjournal.run_journal;

import org.springframework.web.multipart.MultipartFile;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @breif Обрабатывает файл формата GPX 
 */
public class GpxProcessor {

        @Data
        @AllArgsConstructor
        private class TrackPoint{
            private double latitude;
            private double longitude;
            private String time;
            private double speed;
            private double cadence;
            private short hr;
        }

    private class XMLHandler extends DefaultHandler{
        private String lastElementName;
        boolean trackPointFlag;
        TrackPoint curTrackPoint;
        

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
            lastElementName = qName;
            
            if (qName.equals("trkpt")){
                trackPointFlag = true;
                String latitude = attributes.getValue("lat");
                String longitude = attributes.getValue("lon");
                curTrackPoint = new TrackPoint(Double.parseDouble(latitude), Double.parseDouble(longitude), "0", 0, 0, (short)0);
                
                //System.out.println("trkpt: lat=" + latitude + " lon=" + longitude);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException{
            if (qName.equals("trkpt")) {
                trackPointFlag=false;
                System.out.println(curTrackPoint.toString());
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException{
            String information = new String(ch,start,length);
            information = information.replace("\n", "").trim();
            if (!information.isEmpty() && trackPointFlag){
                switch (lastElementName) {
                    case "time":
                        curTrackPoint.setTime(information);
                        //System.out.println("time: " + information);
                        break;
                    case "ns3:speed":
                        curTrackPoint.setSpeed(Double.parseDouble(information));
                        //System.out.println("ns3:speed: " + information);
                        break;
                    case "ns3:cad":
                        curTrackPoint.setCadence(Double.parseDouble(information));
                        //System.out.println("ns3:cad: " + information);
                        break;
                    case "ns3:hr":
                        curTrackPoint.setHr(Short.parseShort(information));
                        //System.out.println("ns3:hr: " + information);
                        break;
                
                    default:
                        break;
                }                    
            }
        }

    }

    

    


    public void processGPX(MultipartFile file){

            System.out.println(file.getName());
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getContentType());
            System.out.println(Long.toString(file.getSize()));

            
            try{
                InputStream is = file.getInputStream();
                //BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                // br.lines().forEach(System.out::println);

                // парсинг XML с помощью SAX
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLHandler handler = new XMLHandler();
                parser.parse(is,handler);           

                

            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }        

    }

}
