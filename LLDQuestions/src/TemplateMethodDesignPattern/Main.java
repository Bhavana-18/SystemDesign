package TemplateMethodDesignPattern;

public class Main {
    public static void main(String[] args){
        System.out.println("Making tea");
        BeverageMaker teaMaker = new TeaMaker();
        teaMaker.makeBeverage();
        System.out.println("Making coffee");
        BeverageMaker coffeeMaker = new CoffeeMaker();
        coffeeMaker.makeBeverage();
    }
}
