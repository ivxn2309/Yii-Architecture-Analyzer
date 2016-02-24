/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is the parent class of all the design pattern entities
 */
package PatternFinder.PatternEntities;

import PatternFinder.Participant;
import java.util.ArrayList;
import java.util.List;

public class DesignPattern {
    public static final int ALL_TIER = 0;
    public static final int DATA_TIER = 1;
    public static final int LOGIC_TIER = 2;
    public static final int PRESENTATION_TIER = 3;
    
    private String name;
    private int tier;
    private List<Participant> parts;

    public DesignPattern(String name, int tier) {
        this.name = name;
        this.tier = tier;
        parts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }
    
    public void addParticipant(Participant part){
        parts.add(part);
    }

    public List<Participant> getParts() {
        return parts;
    }
    
    @Override
    public String toString(){
        String str = "Design Pattern: '" + name + "'" + "\n";
        if(tier != 0) {
            String tierName = "";
            switch(tier) {
                case 1: tierName = "Data";
                break;
                case 2: tierName = "Logic";
                break;
                case 3: tierName = "Presentation";
                break;
            }
            str = str + "From Tier: " + tierName + "\n";
        }
        return str;
    }
}
