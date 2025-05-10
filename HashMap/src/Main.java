public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        HashMap map = new HashMap();
        map.put(1, 20);
        map.put(2, 30);
        System.out.println(map.get(1));
        map.remove(1);
        System.out.println(map.get(1));
    }
}