/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This object analize the files searching for the MVC pattern.
 */

package PatternFinder;
import PatternFinder.PatternEntities.Participant;
import PatternFinder.PatternEntities.DesignPattern;
import PatternFinder.PatternEntities.MVC;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom2.Element;

public class MVCFinder implements Finder {
    private final List<Element> packages;    
    private final MVC pattern;
    private final File root;
    
    public static final int MODEL = 1;
    public static final int VIEW = 2;
    public static final int CONTROLLER = 3;
    
    public MVCFinder(List<Element> packages, File root) {
        this.packages = packages;
        this.root = root;
        pattern = new MVC();
    }

    @Override
    public boolean find() {
        for(Element pack : packages){
            List<Element> classes = pack.getChildren("class");
            for(Element object : classes) {
                String name = object.getAttributeValue("name");
                Element fileDetails = object.getChild("file");
                String path = fileDetails.getAttributeValue("name");
                
                Participant participant;
                switch(isMVC(path)) {
                    case 0:
                        break;
                    case 1:
                        participant = new Participant(name, "model", path);
                        pattern.addModel(participant);
                        break;
                    case 2:
                        participant = new Participant(name, "view", path);
                        pattern.addView(participant);
                        break;
                    case 3:
                        participant = new Participant(name, "controller", path);
                        pattern.addController(participant);
                        break;
                }
            }
        }
        //This adds the views that are not objects
        addViews();
        return !pattern.isEmpty();
    }
    
    /**
     * Determines if the object in the path specified belongs to the MVC pattern
     * @param path The path to the object
     * @return It returns 1 if it belongs to Models, 2 if it is a View, 3 if it is a Controller
     * If this method returns 0 it means that the object does not belongs to MVC.
     */
    private int isMVC(String path) {
        //models
        Pattern regex = Pattern.compile(".+protected[/\\\\]models.+[.]php");
        Matcher matcher = regex.matcher(path);
        if(matcher.find()) return 1;
        
        //views
        regex = Pattern.compile(".+protected[/\\\\]views.+");
        matcher = regex.matcher(path);
        if(matcher.find()) return 2;
        
        //controllers
        regex = Pattern.compile(".+protected[/\\\\]controllers.+[.]php");
        matcher = regex.matcher(path);
        if(matcher.find()) return 3;
        
        return 0;
    }
    
    /**
     * This method searches for the views that are not classes hence they are not 
     * in the summary of objects.
     * 
     * Obtain the root directory and then retrieve the subdirectories 
     * that correspond to views.
     */
    private void addViews() {
        String viewsPath = "/protected/views/";
        File viewsDir = new File(root.getAbsolutePath() + viewsPath);
        if(!viewsDir.exists()) return;
        File [] views = viewsDir.listFiles();
        for(File view : views) {
            pattern.addView(new Participant(view.getName(), "view", view.getAbsolutePath()));
        }
    }
    
    @Override
    public DesignPattern getPattern() {
        pattern.generateAssociations();
        return pattern;
    }
}
