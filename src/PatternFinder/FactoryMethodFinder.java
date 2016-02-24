/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This object analize the files searching for the Factory-Method pattern.
 */

package PatternFinder;

import CodeAnalyzer.CodeReader;
import PatternFinder.PatternEntities.DesignPattern;
import PatternFinder.PatternEntities.FactoryMethod;
import java.io.File;
import java.util.List;
import org.jdom2.Element;

public class FactoryMethodFinder implements Finder {
    private final FactoryMethod pattern;
    private final List<Element> framPackages;
    private final File framework;

    public FactoryMethodFinder(List<Element> framPackages, File framework) {
        this.framPackages = framPackages;
        this.framework = framework;
        pattern = new FactoryMethod();
    }
    
    @Override
    public boolean find() {
        for(Element pack : framPackages){
            List<Element> classes = pack.getChildren("class");
            for(Element object : classes) {
                String name = object.getAttributeValue("name");
                Element fileDetails = object.getChild("file");
                String path = fileDetails.getAttributeValue("name");
                
                //Evaluate the oobjects
                int result = isFactoryMethod(name);
                //If result is 0 means that the object does not belong to the pattern
                if(result != 0) {
                    Participant participant;
                    participant = new Participant(name, "Factory Method", path);
                    
                    //Join the pattern
                    switch(result) {                        
                        case 1: pattern.setFactoryMethod(participant);
                        break;
                        
                        case 2: 
                            participant.setRole("Product");
                            pattern.setProduct(participant);
                        break;
                    }
                }
            }
        }
        locateInterface();
        return !pattern.isEmpty();
    }

    @Override
    public DesignPattern getPattern() {
        return pattern;
    }
    
    /**
     * This method evaluate the objects to know if they belong to such pattern
     * @param name The name of the file
     * @return An integer is returned depending of its name
     */
    private int isFactoryMethod(String name) {
        if(name.matches("CWidgetFactory")) return 1;
        if(name.matches("CWidget")) return 2;
        return 0;
    }
    
    /**
     * The factory method includes an interface that it is not included into the
     * object arrangement, hence we have to search it manually.
     */
    private void locateInterface() {
        if(framework == null) return;
        File interfaces = new File(framework.getAbsolutePath() + "/base/interfaces.php");
        if(!interfaces.canRead()) return;
        CodeReader reader = new CodeReader(interfaces);
        String line = reader.findGroupRegEx("^[^*][\\w\\s]+IWidgetFactory[\\w\\s]*");
        if(line == null) return;
        
        line = line.replace("interface", "");
        line = line.trim();
        
        Participant iFactory = new Participant(line, "Factory", interfaces.getAbsolutePath());
        pattern.setFactory(iFactory);
    }
    
}
