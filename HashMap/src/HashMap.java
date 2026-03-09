import java.util.Map;

public class HashMap {

    Entry[] hashMap ;
    int DEFAULT_SIZE = 100;
    int currentCapacity;
    int LOAD_FACTOR = 0.75;
    int size;
    HashMap(){
        hashMap = new Entry[DEFAULT_SIZE];
        currentCapacity = DEFAULT_SIZE;
        size = 0;
    }
    public int getBucketIndex(int key){
        return Math.abs(Integer.hashCode(key)) % currentCapacity;
    }

    public  int get(int key){
        int index = getBucketIndex(key);
        Entry head = hashMap[index];
        while(head != null){
            if(head.getKey() == key)
                return head.getValue();
            head  = head.next;
        }
      return -1;
    }

    public void put(int key, int value){
        if((double) size/capacity >= LOAD_FACTOR)
            rehash();
        int index = getBucketIndex(key);
        Entry head = hashMap[index];
        Entry curr = head;
        while(curr != null){
            if(curr.getKey() == key){
                curr.setValue(value);
                return;
            }
            curr = curr.next;
        }
        Entry newNode = new Entry(key,value);
        newNode.next = head;
        hashMap[index] = newNode;
        size++;
    }

    public void remove(int key){
        int index = getBucketIndex(key);
        Entry curr = hashMap[index], prev = null;

        while(curr != null){
            if(curr.getKey() == key){
                if(prev == null){
                   hashMap[index] = curr.next;
                } else{
                    prev.next = curr.next;
                }
                return;
            }
            prev = curr;
            curr = curr.next;
        }

    }

    private void rehash(){
        int oldCapacity = currentCapacity;
        currentCapacity = 2 * currentCapacity;
        Entry[] newTable = new Entry[currentCapacity];
        for(int i = 0; i<oldCapacity; i++){
            Entry head = hashMap[i];
            while(head != null){
                Entry next = head.next;
                int newIndex = getBucketIndex(head.getKey());
                head.next = newTable[newIndex];
                newTable[newIndex] = head;
                head = next;
            }
        }

        this.hashMap = newTable;
    }



}
