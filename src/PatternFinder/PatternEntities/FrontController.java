/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is an entity that represents the Front-Controller Design Pattern
 */

package PatternFinder.PatternEntities;

import PatternFinder.Participant;

public class FrontController extends DesignPattern {
    private Participant frontController;
    private Participant dispatcher;

    public FrontController() {
        super("Front Controller", DesignPattern.PRESENTATION_TIER);
    }

    public Participant getFrontController() {
        return frontController;
    }

    public void setFrontController(Participant frontController) {
        this.frontController = frontController;
    }

    public Participant getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Participant dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    public boolean isEmpty() {
        if(frontController == null)
            return true;
        return dispatcher == null;
    }
    
    @Override
    public String toString() {
        String fc = super.toString();
        fc = fc + "FrontController > " + frontController + "\n";
        fc = fc + "Dispatcher > " + dispatcher;
        return fc;
    }
    
}
