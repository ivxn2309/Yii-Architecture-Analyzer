/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This class will create xml files from a list of design patterns.
 * 
 */
package ResultsManager;

import PatternFinder.PatternEntities.Participant;
import PatternFinder.PatternEntities.DesignPattern;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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
            txt.append("<analysis timestamp='").append(timestamp).append("' ");
            txt.append("parent='patterns' child='pattern' grandson='part' greatgrandson='association'").append(">\n");
            txt.append(generatePatternListXML());
            txt.append("</analysis>");
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
        xml.append("\t<patterns>\n");
        //xml.append("\t\t<pattern name='Layers' tier='1,2,3' mvc='0' /> \n");
        for(DesignPattern pattern : patterns) {
            xml.append(generatePatternXML(pattern)).append("\n");
        }
        xml.append("\t</patterns>\n");
        return xml.toString();
    }
    
    private String generatePatternXML(DesignPattern pattern) {
        StringBuilder xml = new StringBuilder();
        xml.append("\t\t")
                .append("<pattern name='").append(pattern.getName()).append("' ")
                .append("tier='").append(pattern.getTier()).append("' ")
                .append("mvc='").append(pattern.getMvc()).append("' ")
                .append(">").append("\n");
        for(Participant p : pattern.getParts()) {
            xml.append("\t\t\t")
                    .append("<part name='").append(p.getName()).append("' ")
                    .append("role='").append(p.getRole()).append("' ")
                    .append("path='").append(p.getPath()).append("'");
            
            if(p.getAssociations() != null) {
                xml.append("> \n");
                String [] associations = p.getAssociations().toString().replaceAll("[\\[\\]\\s]", "").split(",");
                for(String obj: associations) {
                    xml.append("\t\t\t\t<association>");
                    xml.append(obj);
                    xml.append("</association>\n");
                }
                xml.append("\t\t\t</part>\n");
            }
            else {
                xml.append(" /> \n");
            }
        }
        xml.append("\t\t</pattern>");
        return xml.toString();
    }
    
}
