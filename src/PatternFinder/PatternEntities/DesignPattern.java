/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is the parent class of all the design pattern entities
 */
package PatternFinder.PatternEntities;

import java.util.ArrayList;
import java.util.List;

public class DesignPattern {
    public static final String NONE = "0";
    
    public static final String ALL_TIER = "1,2,3";
    public static final String DATA_TIER = "1";
    public static final String LOGIC_TIER = "2";
    public static final String PRESENTATION_TIER = "3";

    public static final String ALL_MVC = "1,2,3";
    public static final String MODEL_MVC = "1";
    public static final String VIEW_MVC = "2";
    public static final String CONTROL_MVC = "3";
    
    private String name;
    private String tier;
    private String mvc;
    
    private List<Participant> parts;

    public DesignPattern(String name, String tier, String mvc) {
        this.name = name;
        this.tier = tier;
        this.mvc = mvc;
        parts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getMvc() {
        return mvc;
    }

    public void setMvc(String mvc) {
        this.mvc = mvc;
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
        if(!tier.equals("1,2,3")) {
            String tierName = "";
            if(tier.contains("1")) tierName = "Data ";
            if(tier.contains("2")) tierName = "Logic ";
            if(tier.contains("3")) tierName = "Presentation ";
            if(!tierName.trim().equals(""))
                str = str + "From Layer: " + tierName + "\n";
        }
        return str;
    }
}
