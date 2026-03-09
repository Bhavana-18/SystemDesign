package TemplateMethodDesignPattern;

public class CoffeeMaker  extends  BeverageMaker{
    @Override
    void brew() {
        System.out.println("Brewing Coffee");
    }

    @Override
    void addCondiments() {
        System.out.println("add coffee powder");

    }
}
