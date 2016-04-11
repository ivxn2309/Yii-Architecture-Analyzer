/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is an entity that represents the Singleton Design Pattern
 */

package PatternFinder.PatternEntities;

public class Singleton extends DesignPattern {
    private Participant singleton;

    public Singleton() {
        super("Singleton", DesignPattern.PRESENTATION_TIER, DesignPattern.NONE);
        this.singleton = null;
    }

    public Participant getSingleton() {
        return singleton;
    }

    public void setSingleton(Participant singleton) {
        this.singleton = singleton;
        super.addParticipant(singleton);
    }
    
    public boolean isEmpty() {
        return singleton == null;
    }
    
    @Override
    public String toString() {
        String single = super.toString();
        single = single + "Singleton > " + singleton;
        return single;
    }
    
    public void generateAssociations() { 
        //This pattern has not association between its participants
    }
}
