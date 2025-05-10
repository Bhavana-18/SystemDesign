public class Entry {
    private int key;
    private int value;
    public Entry next;

    Entry(int key , int value){
        this.key = key;
        this.value = value;
        this.next = null;
    }

    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
