/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This object analize the files searching for the Front-Controller pattern.
 */

package PatternFinder;

import PatternFinder.PatternEntities.Participant;
import CodeAnalyzer.CodeReader;
import PatternFinder.PatternEntities.DesignPattern;
import PatternFinder.PatternEntities.FrontController;
import java.io.File;

public class FrontControllerFinder implements Finder {
    private final FrontController  pattern;
    private final String pathToIndex;

    public FrontControllerFinder(String pathToIndex) {
        this.pattern = new FrontController();
        this.pathToIndex = pathToIndex;
    }
    
    @Override
    public boolean find() {
        File index = new File(pathToIndex);
        if(!index.canRead()) return false;
        
        CodeReader reader = new CodeReader(index);
        //Check the call to the application config 
        String configLine = reader.findGroupRegEx(".*createWebApplication.*[(]{1}(.*)[)]{1}->run.*", 1);
        String locationLine;
        
        //If there is not a file config declared in the index
        if(configLine == null || configLine.trim().equals("")) return false;
        
        //If it contains $ means that it is being called with a variable
        if(configLine.contains("$"))
            locationLine = reader.findGroupRegEx(".*"+configLine.replace("$", "[$]")+".*=.*'/(.*)'.*[;].*", 1);
        
        //Else it has one reference directly to the directory
        else {
            locationLine = configLine.replaceAll("__FILE__[().]*", "");
            locationLine = locationLine.replaceAll("['\"\\\\]", "");
            locationLine = locationLine.substring(1);
        }
        
        if(locationLine == null) return false;
        
        //Create a reference to the config file
        String absPath = index.getAbsolutePath();
        absPath = absPath.replace("index.php", locationLine);
        File config = new File(absPath);
        
        //If it is a valid file then read it
        if(!config.canRead()) return false;
        reader = new CodeReader(config);
        
        //The array called 'urlManager' manages the front controller in the application
        String line = reader.findGroupRegEx(".*urlManager.*");
        if(line == null) return false;
        
        //Join the pattern
        pattern.setFrontController(new Participant(index.getName(), "Front Controller", index.getAbsolutePath()));
        pattern.setDispatcher(new Participant(config.getName(), "Dispatcher", config.getAbsolutePath()));
        return true;
    }

    @Override
    public DesignPattern getPattern() {
        pattern.generateAssociations();
        return pattern;
    }
    
}
