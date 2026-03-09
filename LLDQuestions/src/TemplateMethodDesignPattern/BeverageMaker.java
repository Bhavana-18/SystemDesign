package TemplateMethodDesignPattern;

public  abstract class BeverageMaker {
    void makeBeverage(){
        this.boilWater();
        this.brew();
        this.pourInCup();
        this.addCondiments();

    }

    abstract void brew();
    abstract void addCondiments();

    void boilWater(){
        System.out.println("BoilingWater");
    }

    void pourInCup(){
        System.out.println("Pouring int cup");
    }
}
