/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is an entity that represents the ORM Design Pattern
 */
package PatternFinder.PatternEntities;

import java.util.ArrayList;
import java.util.List;

public class ORM extends DesignPattern {
    private List<Participant> entities;
    private Participant activeRecord;
    
    private Participant dataSource;

    public ORM() {
        super("Object Relational Mapper", DesignPattern.DATA_TIER, DesignPattern.MODEL_MVC);
        entities = new ArrayList<>();
        activeRecord = null;
        
        dataSource = new Participant("Data Source", "Data Source");
    }

    public Participant getActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(Participant activeRecord) {
        this.activeRecord = activeRecord;
        super.addParticipant(activeRecord);
    }

    public List<Participant> getEntities() {
        return entities;
    }
    
    public void addEntity(Participant entity) {
        entities.add(entity);
        super.addParticipant(entity);
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
    
    public void generateAssociations() { 
        dataSource.addAssociation(activeRecord);
        activeRecord.addAssociation(dataSource);
        
        for(Participant part : entities)
            activeRecord.addAssociation(part);
    }
}
