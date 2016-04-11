/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This object analize the files searching for the Singleton pattern.
 */

package PatternFinder;

import PatternFinder.PatternEntities.Participant;
import CodeAnalyzer.CodeReader;
import PatternFinder.PatternEntities.DesignPattern;
import java.io.File;
import PatternFinder.PatternEntities.Singleton;

public class SingletonFinder implements Finder {
    private final String pathToIndex;
    private final Singleton pattern;

    public SingletonFinder(String pathToIndex) {
        this.pathToIndex = pathToIndex;
        pattern = new Singleton();
    }

    @Override
    public boolean find() {
        File file = new File(pathToIndex);
        CodeReader reader = new CodeReader(file);
        if(!file.canRead()) return false;
        
        String locationLine = reader.findGroupRegEx(".*[$]yii.*=.*[;].*");
        String concreteLocation = reader.getValueFromVar("yii", locationLine);
        if(concreteLocation == null) return false;
        concreteLocation = concreteLocation.replaceAll("dirname[\\s]*[(][\\s]*__FILE__[\\s]*[)][\\s]*[.]{1}[/]{1}", "");
        
        String absPath = file.getAbsolutePath();
        absPath = absPath.replace("index.php", concreteLocation);
        file = new File(absPath);
        
        if(!file.canRead()) return false;
        reader = new CodeReader(file);
        
        String baseName = reader.findGroupRegEx("[\\s]*class[\\s]*Yii[\\s]*extends[\\s]+[\\w]*[\\s]*");
        if(baseName == null) return false;
        baseName = baseName.replaceAll("[\\s]*class[\\s\\w]*extends", "");
        baseName = baseName.trim();
        
        String lineInherit = reader.findGroupRegEx("[\\s]*require[\\s]*[(]{1}[\\s]*dirname[\\s]*[(][\\s]*__FILE__[\\s]*[)][\\s]*[.][\\s]*.*" + baseName + ".*[\\s]*[)][\\s]*[;]{1}[\\s]*");
        if(lineInherit == null) return false;
        lineInherit = lineInherit.replaceAll("[\\s]*require[\\s]*[(][\\s]*dirname[\\s]*[(][\\s]*__FILE__[\\s]*[)][\\s]*[.][\\s]*", "");
        lineInherit = lineInherit.replaceAll("[()'\";/]", "");
        
        String baseLocation = file.getAbsolutePath();
        baseLocation = baseLocation.replace("yii.php", "");
        baseLocation = baseLocation + lineInherit;
        file = new File(baseLocation);
        
        if(!file.exists()) return false;
        
        Participant singletonPart = new Participant(baseName, "Singleton", file.getAbsolutePath());
        pattern.setSingleton(singletonPart);
        
        return true;
    }
    
    @Override
    public DesignPattern getPattern() {
        pattern.generateAssociations();
        return pattern;
    }
    
}
