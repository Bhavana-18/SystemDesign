public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        LogProcessor logProcessor = new InfoLogProcessor(new DebugLogProcessor(new ErrorLogProcessor(null)));
        logProcessor.log(3, "Error Found");

    }
}