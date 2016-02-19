/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This interface defines the structure of all the finders of patterns, 
 * there will be a finder for each pattern then is necessary one standard.
 */

package PatternFinder;
import PatternFinder.PatternEntities.DesignPattern;

public interface Finder {
        
    /**
     * This does a quick analysis, if there are directories and files where they 
     * have to, and it also makes revision of code if neccesary.
     * 
     * @return A boolean value, true if probably the pattern was implemented or false if surely the pattern was not implemented.
     */
    public boolean find();
 
    
    /**
     * This method gives the pattern found in the project
     * @return A design pattern that can be any of the pattern entities.
     */
    public DesignPattern getPattern();
}
