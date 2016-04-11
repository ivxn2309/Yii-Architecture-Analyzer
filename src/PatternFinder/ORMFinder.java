/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This object analize the files searching for the ORM pattern.
 */

package PatternFinder;

import PatternFinder.PatternEntities.Participant;
import PatternFinder.PatternEntities.DesignPattern;
import PatternFinder.PatternEntities.ORM;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom2.Element;

public class ORMFinder implements Finder {
    private final List<Element> projPackages;
    private final List<Element> framPackages;
    private final ORM pattern;

    public ORMFinder(List<Element> projPackages, List<Element> framPackages) {
        this.projPackages = projPackages;
        this.framPackages = framPackages;
        pattern = new ORM();
    }

    @Override
    public boolean find() {
        projPackages.stream().map((pack) -> pack.getChildren("class")).forEach((classes) -> {
            classes.stream().forEach((object) -> {
                String name = object.getAttributeValue("name");
                Element fileDetails = object.getChild("file");
                String path = fileDetails.getAttributeValue("name");
                
                Participant participant;
                if (isEntity(path)) {
                    participant = new Participant(name, "entity", path);
                    pattern.addEntity(participant);
                }
            });
        });
        
        for(Element pack : framPackages){
            List<Element> classes = pack.getChildren("class");
            for(Element object : classes) {
                String name = object.getAttributeValue("name");
                Element fileDetails = object.getChild("file");
                String path = fileDetails.getAttributeValue("name");
                
                Participant participant;
                if (isActiveRecord(path)) {
                    participant = new Participant(name, "ORM", path);
                    pattern.setActiveRecord(participant);
                    return !pattern.isEmpty();
                }
            }
        }
        
        return !pattern.isEmpty();
    }
    
    /**
     * It determines if the specified path belongs to a model because an entity 
     * and a model is the same for yii
     * @param path The path of the file that contains the object
     * @return A boolean value that represents if is an entity or not
     */
    private boolean isEntity(String path) {
        Pattern regex = Pattern.compile(".+protected[/\\\\]models.+[.]php");
        Matcher matcher = regex.matcher(path);
        return matcher.find();
    }
    
    /**
     * it searches for the CActiveRecord using the path of the file
     * @param path The path of the file
     * @return A boolean value, true if it found the active record else returns false
     */
    private boolean isActiveRecord(String path) {
        Pattern regex = Pattern.compile(".+db[/\\\\].+[/\\\\].*CActiveRecord[.]{1}php");
        Matcher matcher = regex.matcher(path);
        return matcher.find();
    }
    
    @Override
    public DesignPattern getPattern() {
        pattern.generateAssociations();
        return pattern;
    }
    
}
