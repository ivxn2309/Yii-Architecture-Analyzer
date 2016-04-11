/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is an entity that represents the MVC Design Pattern
 */

package PatternFinder.PatternEntities;

import java.util.ArrayList;
import java.util.List;

public class MVC extends DesignPattern {
    private List<Participant> models;
    private List<Participant> views;
    private List<Participant> controllers;

    public MVC() {
        super("MVC", DesignPattern.NONE, DesignPattern.ALL_MVC);
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
    
    public void generateAssociations() { 
        boolean viewsAdded = false;
        for(Participant model: models) {
            String modelName = model.getName();
            for(Participant controller: controllers) {
                String contrName = controller.getName().replace("Controller", "");
                if(contrName.equals(modelName)) {
                    model.addAssociation(controller);
                    controller.addAssociation(model);
                }
                if(!viewsAdded) {
                    for(Participant view: views) {
                        if(contrName.equalsIgnoreCase(view.getName())) {
                            controller.addAssociation(view);
                            view.addAssociation(controller);
                        }
                    }
                }
            }
            viewsAdded = true;            
        }
    }
}
