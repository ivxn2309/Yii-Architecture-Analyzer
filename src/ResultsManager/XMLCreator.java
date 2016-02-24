/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This class will create xml files from a list of design patterns.
 * 
 */
package ResultsManager;

import PatternFinder.Participant;
import PatternFinder.PatternEntities.DesignPattern;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLCreator {
    private final List<DesignPattern> patterns;

    public XMLCreator(List<DesignPattern> patterns) {
        this.patterns = patterns;
    }
    
    public File createXMLFile(String fileName) {
        try {
            StringBuilder txt = new StringBuilder();
            txt.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            String timestamp = Calendar.getInstance().getTime().toString();
            txt.append("<analysis timestamp='").append(timestamp).append("' />\n");
            txt.append(generatePatternListXML());
            File file = new File(fileName);
            file.createNewFile();
            fillFile(file, txt.toString());
            return file;
        }
        catch (IOException ex) {
            ex.getMessage();
        }
        return null;
    }
    
    private boolean fillFile(File file, String text) {
        if(file.canWrite()) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.append(text);
            } 
            catch (IOException ex) {
                ex.getMessage();
            }
            finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.getMessage();
                }
            }
        }
        return false;
    }

    public String generatePatternListXML() {
        StringBuilder xml = new StringBuilder();
        xml.append("<patterns>\n");
        for(DesignPattern pattern : patterns) {
            xml.append(generatePatternXML(pattern)).append("\n");
        }
        xml.append("</patterns>\n");
        return xml.toString();
    }
    
    private String generatePatternXML(DesignPattern pattern) {
        StringBuilder xml = new StringBuilder();
        xml.append("\t")
                .append("<pattern name='")
                .append(pattern.getName())
                .append("' tier='")
                .append(pattern.getTier())
                .append("'>")
                .append("\n");
        for(Participant p : pattern.getParts()) {
            xml.append("\t\t")
                    .append("<part name='")
                    .append(p.getName())
                    .append("' role='")
                    .append(p.getRole())
                    .append("' path='")
                    .append(p.getPath())
                    .append("' />")
                    .append("\n");
        }
        xml.append("\t</pattern>");
        return xml.toString();
    }
    
}
