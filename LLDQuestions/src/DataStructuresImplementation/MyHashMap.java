package DataStructuresImplementation;

import java.util.Objects; // For Objects.equals and Objects.hash

public class MyHashMap<K, V> {

    // --- 1. Node Class ---
    // Represents a single key-value pair in the hash map, linked for separate chaining.
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next; // Pointer for separate chaining

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public String toString() {
            return "{" + key + "=" + value + "}";
        }
    }

    // --- 2. Array of Nodes (Buckets) ---
    private Node<K, V>[] buckets;
    private int size; // Number of key-value pairs
    private int capacity; // Current size of the buckets array
    private static final int DEFAULT_CAPACITY = 16; // Initial capacity
    private static final float DEFAULT_LOAD_FACTOR = 0.75f; // Threshold for resizing
    private float loadFactor;

    // --- Constructors ---
    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked") // Suppress warning for generic array creation
    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive and a number.");
        }
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.buckets = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
    }

    // --- 3. Hash Function ---
    // Calculates the bucket index for a given key.
    private int getBucketIndex(K key) {
        // Handle null keys: often mapped to bucket 0, or throw NullPointerException
        // For simplicity, we'll let Objects.hashCode handle it, which returns 0 for null.
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    // --- Core Operations ---

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is replaced.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     * @return The previous value associated with key, or null if there was no mapping for key.
     */
    public V put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = buckets[bucketIndex];

        // Traverse the linked list at the bucket to check if key already exists
        Node<K, V> current = head;
        while (current != null) {
            // Using Objects.equals to handle null keys gracefully
            if (Objects.equals(current.key, key)) {
                V oldValue = current.value;
                current.value = value; // Update existing value
                return oldValue;
            }
            current = current.next;
        }

        // Key not found, so add a new node at the beginning of the list
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = head; // Link new node to the old head
        buckets[bucketIndex] = newNode; // Set new node as the head
        size++;

        // --- Resizing (Rehashing) ---
        // Check if load factor is exceeded, and resize if necessary
        if ((float) size / capacity > loadFactor) {
            resize();
        }

        return null; // No old value was replaced
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or `null` if this map contains no mapping for the key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value to which the specified key is mapped, or `null`.
     */
    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = buckets[bucketIndex];

        // Traverse the linked list to find the key
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null; // Key not found
    }

    /**
     * Removes the mapping for the specified key from this map if it is present.
     *
     * @param key The key whose mapping is to be removed from the map.
     * @return The previous value associated with key, or `null` if there was no mapping for key.
     */
    public V remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = buckets[bucketIndex];

        // Special case: if the head node is the one to be removed
        if (head != null && Objects.equals(head.key, key)) {
            V oldValue = head.value;
            buckets[bucketIndex] = head.next; // Update head of the list
            size--;
            return oldValue;
        }

        // Traverse the list to find the node to be removed
        Node<K, V> current = head;
        Node<K, V> prev = null;
        while (current != null && !Objects.equals(current.key, key)) {
            prev = current;
            current = current.next;
        }

        // If key was found
        if (current != null) {
            prev.next = current.next; // Bypass the current node
            size--;
            return current.value;
        }

        return null; // Key not found
    }

    /**
     * Returns `true` if this map contains a mapping for the specified key.
     *
     * @param key The key whose presence in this map is to be tested.
     * @return `true` if this map contains a mapping for the specified key.
     */
    public boolean containsKey(K key) {
        return get(key) != null; // Simpler to use get() method
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    public int size() {
        return size;
    }

    /**
     * Returns `true` if this map contains no key-value mappings.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    // --- 10. Resizing (Rehashing) ---
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2; // Double the capacity
        Node<K, V>[] oldBuckets = buckets;
        this.buckets = (Node<K, V>[]) new Node[capacity]; // Create new, larger array
        this.size = 0; // Reset size as elements will be re-added

        System.out.println("Resizing HashMap: Old Capacity = " + oldCapacity + ", New Capacity = " + capacity);

        // Rehash all existing elements into the new buckets
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> current = oldBuckets[i];
            while (current != null) {
                // Store next node before re-hashing current
                Node<K, V> nextNode = current.next;
                // Add to the new hash map (this implicitly uses the new capacity)
                put(current.key, current.value);
                current = nextNode;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            Node<K, V> current = buckets[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key).append("=").append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // --- Main Method for Demonstration ---
    public static void main(String[] args) {
        MyHashMap<String, Integer> myMap = new MyHashMap<>();

        System.out.println("Initial size: " + myMap.size() + ", Is empty: " + myMap.isEmpty()); // 0, true

        // --- PUT operations ---
        System.out.println("\n--- PUT Operations ---");
        myMap.put("Apple", 1);
        myMap.put("Banana", 2);
        myMap.put("Cherry", 3);
        myMap.put("Date", 4);
        myMap.put("Elderberry", 5);
        myMap.put("Fig", 6);
        myMap.put("Grape", 7);
        myMap.put("Honeydew", 8); // This might trigger resize based on default capacity/load factor
        myMap.put("Grape", 9); // Update existing key

        System.out.println("Map after puts: " + myMap);
        System.out.println("Size: " + myMap.size()); // Should be 8 (Grape updated)

        // --- GET operations ---
        System.out.println("\n--- GET Operations ---");
        System.out.println("Value for Apple: " + myMap.get("Apple"));     // 1
        System.out.println("Value for Grape: " + myMap.get("Grape"));     // 9 (updated value)
        System.out.println("Value for Kiwi: " + myMap.get("Kiwi"));       // null (not found)

        // --- CONTAINS KEY operations ---
        System.out.println("\n--- CONTAINS KEY Operations ---");
        System.out.println("Contains 'Banana'? " + myMap.containsKey("Banana"));   // true
        System.out.println("Contains 'Kiwi'? " + myMap.containsKey("Kiwi"));     // false

        // --- REMOVE operations ---
        System.out.println("\n--- REMOVE Operations ---");
        System.out.println("Removing 'Banana'. Old value: " + myMap.remove("Banana")); // 2
        System.out.println("Map after removing Banana: " + myMap);
        System.out.println("Size after remove: " + myMap.size()); // 7
        System.out.println("Removing 'Kiwi'. Old value: " + myMap.remove("Kiwi"));   // null (not found)
        System.out.println("Size after removing non-existent: " + myMap.size()); // Still 7

        // Test with null key
        System.out.println("\n--- Null Key Test ---");
        myMap.put(null, 100);
        System.out.println("Map with null key: " + myMap);
        System.out.println("Value for null: " + myMap.get(null)); // 100
        System.out.println("Removing null. Old value: " + myMap.remove(null)); // 100
        System.out.println("Map after removing null: " + myMap);
        System.out.println("Size: " + myMap.size());

        // Fill up to trigger more resizes
        System.out.println("\n--- Filling up to trigger more resizes ---");
        for (int i = 0; i < 20; i++) {
            myMap.put("Key" + i, i * 10);
        }
        System.out.println("Map after more puts (might be very long): " + myMap);
        System.out.println("Final size: " + myMap.size() + ", Is empty: " + myMap.isEmpty()); // Should be 27, false
    }
}
