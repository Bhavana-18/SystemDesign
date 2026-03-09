public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        BrowserHistoryService service = new BrowserHistoryService(3);

        String user = "user1";

        // Visit pages
        service.visit(user, "A");
        service.visit(user, "B");
        service.visit(user, "C");

        // Go back
        System.out.println(service.back(user)); // prints B
        System.out.println(service.back(user)); // prints A
        System.out.println(service.back(user)); // still prints A (no more back)

        // Forward
        System.out.println(service.forward(user)); // prints B
        System.out.println(service.forward(user)); // prints C
        System.out.println(service.forward(user)); // still prints C (no more forward)

        // Visit a new page, forward history should clear
        service.visit(user, "D");
        System.out.println(service.back(user));    // prints C
        System.out.println(service.forward(user)); // prints D

        // Remove user (cleanup)
        service.removeUser(user);
    }
}