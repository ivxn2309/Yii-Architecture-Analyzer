/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This object analize the files searching for the ORM pattern.
 */

package PatternFinder;

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
                    pattern.entities.add(participant);
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
    
    private boolean isEntity(String path) {
        Pattern regex = Pattern.compile(".+protected[/\\\\]models.+[.]php");
        Matcher matcher = regex.matcher(path);
        return matcher.find();
    }
    
    private boolean isActiveRecord(String path) {
        Pattern regex = Pattern.compile(".+db[/\\\\].+[/\\\\].*CActiveRecord[.]{1}php");
        Matcher matcher = regex.matcher(path);
        return matcher.find();
    }
    
    @Override
    public DesignPattern getPattern() {
        return pattern;
    }
    
}
