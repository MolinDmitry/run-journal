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

    private class XMLHandler extends DefaultHandler{
        private String lastElementName;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
            lastElementName = qName;
            
            if (qName.equals("trkpt")){
                String latitude = attributes.getValue("lat");
                String longitude = attributes.getValue("lon");
                System.out.println("trkpt: lat=" + latitude + " lon=" + longitude);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException{
            String information = new String(ch,start,length);
            information = information.replace("\n", "").trim();
            if (!information.isEmpty()){
                switch (lastElementName) {
                    case "time":
                        System.out.println("time: " + information);
                        break;
                    case "ns3:speed":
                        System.out.println("ns3:speed: " + information);
                        break;
                    case "ns3:cad":
                        System.out.println("ns3:cad: " + information);
                        break;
                    case "ns3:hr":
                        System.out.println("ns3:hr: " + information);
                        break;
                
                    default:
                        break;
                }                    
            }
        }

    }

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
