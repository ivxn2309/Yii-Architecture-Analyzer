/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is an entity that represents the ORM Design Pattern
 */
package PatternFinder.PatternEntities;

import PatternFinder.Participant;
import java.util.ArrayList;
import java.util.List;

public class ORM extends DesignPattern {
    public List<Participant> entities;
    private Participant activeRecord;

    public ORM() {
        super("Object Relational Mapper", DesignPattern.DATA_TIER);
        entities = new ArrayList<>();
        activeRecord = null;
    }

    public Participant getActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(Participant activeRecord) {
        this.activeRecord = activeRecord;
    }

    public boolean isEmpty() {
        if(activeRecord == null)
            return true;
        return entities.isEmpty();        
    }
    
    @Override
    public String toString() {
        String orm = super.toString();
        orm = orm + "Entities > " + entities + "\n";
        orm = orm + "ActiveRecord > " + activeRecord;
        return orm;
    }
}
