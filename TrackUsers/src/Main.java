public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        TrackUsers trackUsers = new TrackUsers();
        trackUsers.addUsers("A");
        trackUsers.addUsers("B");
        trackUsers.addUsers("A");
        System.out.println(trackUsers.getFirstUser());
    }
}