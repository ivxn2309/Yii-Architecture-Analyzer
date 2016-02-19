/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This is an entity that represents the Factory-Method Design Pattern
 */

package PatternFinder.PatternEntities;

import PatternFinder.Participant;

public class FactoryMethod extends DesignPattern {
    private Participant factory;
    private Participant factoryMethod;
    private Participant product;

    public FactoryMethod() {
        super("Factory Method", DesignPattern.PRESENTATION_TIER);
    }

    public Participant getFactory() {
        return factory;
    }

    public void setFactory(Participant factory) {
        this.factory = factory;
    }

    public Participant getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(Participant factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public Participant getProduct() {
        return product;
    }

    public void setProduct(Participant product) {
        this.product = product;
    }
    
    public boolean isEmpty() {
        if(factory == null)
            return true;
        if(factoryMethod == null)
            return true;
        return product == null;
    }
    
    @Override
    public String toString() {
        String fm = super.toString();
        fm = fm + "Factory > " + factory + "\n";
        fm = fm + "Factory Method > " + factoryMethod + "\n";
        fm = fm + "Product > " + product;
        return fm;
    }
}
