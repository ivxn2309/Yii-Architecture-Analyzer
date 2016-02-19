/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is an entity that represents the MVC Design Pattern
 */

package PatternFinder.PatternEntities;

import PatternFinder.Participant;
import java.util.ArrayList;
import java.util.List;

public class MVC extends DesignPattern {
    public List<Participant> models;
    public List<Participant> views;
    public List<Participant> controllers;

    public MVC() {
        super("MVC", DesignPattern.ALL_TIER);
        models = new ArrayList<>();
        views = new ArrayList<>();
        controllers = new ArrayList<>();
    }
    
    public boolean isEmpty() {
        if(!models.isEmpty())
            return false;
        if(!views.isEmpty())
            return false;
        if(!controllers.isEmpty())
            return false;        
        return true;
    }
    
    @Override
    public String toString() {
        String mvc = super.toString();
        mvc = mvc + "Models > " + models + "\n";
        mvc = mvc + "Views > " + views + "\n";
        mvc = mvc + "Controllers > " + controllers;
        return mvc;
    }
    
}
