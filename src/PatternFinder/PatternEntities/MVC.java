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
    private List<Participant> models;
    private List<Participant> views;
    private List<Participant> controllers;

    public MVC() {
        super("MVC", DesignPattern.ALL_TIER);
        models = new ArrayList<>();
        views = new ArrayList<>();
        controllers = new ArrayList<>();
    }

    public List<Participant> getModels() {
        return models;
    }

    public List<Participant> getViews() {
        return views;
    }

    public List<Participant> getControllers() {
        return controllers;
    }
    
    public void addModel(Participant model) {
        models.add(model);
        super.addParticipant(model);
    }
    
    public void addView(Participant view) {
        views.add(view);
        super.addParticipant(view);
    }
    
    public void addController(Participant controller) {
        controllers.add(controller);
        super.addParticipant(controller);
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
