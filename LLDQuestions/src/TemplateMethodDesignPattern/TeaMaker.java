package TemplateMethodDesignPattern;

public class TeaMaker extends  BeverageMaker{
    @Override
    void brew() {
        System.out.println("tea brewing");
    }

    @Override
    void addCondiments() {
        System.out.println("add a tea bag");

    }
}
