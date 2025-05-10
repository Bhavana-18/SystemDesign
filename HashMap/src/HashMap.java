public class HashMap {

    Entry[] hashMap ;
    int DEFAULT_SIZE = 100;

    HashMap(){
        hashMap = new Entry[DEFAULT_SIZE];
    }
    public int getBucketIndex(int key){
        return Integer.hashCode(key) % DEFAULT_SIZE;
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

}
