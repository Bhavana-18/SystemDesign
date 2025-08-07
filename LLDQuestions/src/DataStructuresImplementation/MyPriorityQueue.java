package DataStructuresImplementation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> {

    private List<E> heap;
    private Comparator<? super E> comparator;

    // --- Constructors ---

    /**
     * Creates an empty MyPriorityQueue with natural ordering.
     * Elements must implement Comparable.
     */
    public MyPriorityQueue() {
        this.heap = new ArrayList<>();
        this.comparator = null; // Use natural ordering
    }

    /**
     * Creates an empty MyPriorityQueue with the specified comparator.
     */
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }

    /**
     * Creates a MyPriorityQueue containing the elements in the specified collection.
     * The elements are added to the priority queue using the offer method.
     */
    public MyPriorityQueue(List<E> collection) {
        this(); // Call default constructor for natural ordering
        for (E element : collection) {
            offer(element);
        }
    }

    public MyPriorityQueue(List<E> collection, Comparator<? super E> comparator) {
        this(comparator); // Call comparator constructor
        for (E element : collection) {
            offer(element);
        }
    }

    // --- Core Operations ---

    /**
     * Inserts the specified element into this priority queue.
     * O(log N) time complexity.
     */
    public void offer(E element) {
        if (element == null) {
            throw new NullPointerException("Priority Queue does not accept null elements.");
        }
        heap.add(element); // Add to the end
        heapifyUp(heap.size() - 1); // Maintain heap property by sifting up
    }

    /**
     * Retrieves and removes the smallest element (head) of this priority queue.
     * Returns null if this queue is empty.
     * O(log N) time complexity.
     */
    public E poll() {
        if (isEmpty()) {
            return null; // As per Queue interface contract for poll
        }
        return removeMin();
    }

    /**
     * Retrieves and removes the smallest element (head) of this priority queue.
     * Throws NoSuchElementException if this queue is empty.
     * O(log N) time complexity.
     */
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority Queue is empty.");
        }
        return removeMin();
    }

    /**
     * Retrieves, but does not remove, the smallest element (head) of this priority queue.
     * Returns null if this queue is empty.
     * O(1) time complexity.
     */
    public E peek() {
        if (isEmpty()) {
            return null; // As per Queue interface contract for peek
        }
        return heap.get(0);
    }

    /**
     * Retrieves, but does not remove, the smallest element (head) of this priority queue.
     * Throws NoSuchElementException if this queue is empty.
     * O(1) time complexity.
     */
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority Queue is empty.");
        }
        return heap.get(0);
    }

    /**
     * Returns the number of elements in this priority queue.
     */
    public int size() {
        return heap.size();
    }

    /**
     * Returns true if this priority queue contains no elements.
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Removes all of the elements from this priority queue.
     */
    public void clear() {
        heap.clear();
    }

    // --- Helper Methods for Heap Maintenance ---

    // Common logic for poll() and remove()
    private E removeMin() {
        E minElement = heap.get(0); // Smallest element is always at the root

        int lastIndex = heap.size() - 1;
        // Move the last element to the root position
        E lastElement = heap.remove(lastIndex); // Remove from the end
        if (!heap.isEmpty()) { // Only if there are elements left
            heap.set(0, lastElement); // Place it at the root
            heapifyDown(0); // Sift down to maintain heap property
        }
        return minElement;
    }

    /**
     * Moves an element up the heap to restore the min-heap property.
     * Called after adding a new element.
     */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2; // Calculate parent index
            if (compare(heap.get(index), heap.get(parentIndex)) < 0) {
                // If current element is smaller than its parent, swap them
                swap(index, parentIndex);
                index = parentIndex; // Continue checking up the tree
            } else {
                // Heap property satisfied
                break;
            }
        }
    }

    /**
     * Moves an element down the heap to restore the min-heap property.
     * Called after removing the root element.
     */
    private void heapifyDown(int index) {
        while (true) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int smallestChildIndex = index; // Assume current node is the smallest

            // Find the smallest among current node, left child, and right child
            if (leftChildIndex < heap.size() && compare(heap.get(leftChildIndex), heap.get(smallestChildIndex)) < 0) {
                smallestChildIndex = leftChildIndex;
            }
            if (rightChildIndex < heap.size() && compare(heap.get(rightChildIndex), heap.get(smallestChildIndex)) < 0) {
                smallestChildIndex = rightChildIndex;
            }

            if (smallestChildIndex != index) {
                // If a child is smaller than current, swap with the smallest child
                swap(index, smallestChildIndex);
                index = smallestChildIndex; // Continue sifting down
            } else {
                // Heap property satisfied
                break;
            }
        }
    }

    /**
     * Swaps two elements in the heap ArrayList.
     */
    private void swap(int i, int j) {
        E temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Compares two elements using the provided comparator or natural ordering.
     */
    @SuppressWarnings("unchecked") // Suppress warning for raw Comparable cast
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            // Use natural ordering (requires E to implement Comparable)
            if (!(a instanceof Comparable)) {
                throw new ClassCastException("Elements must be Comparable or a Comparator must be provided.");
            }
            return ((Comparable<E>) a).compareTo(b);
        }
    }

    @Override
    public String toString() {
        return heap.toString();
    }

    // --- Main Method for Demonstration ---
    public static void main(String[] args) {
        System.out.println("--- Natural Ordering (Min-Heap) ---");
        MyPriorityQueue<Integer> minHeap = new MyPriorityQueue<>();
        minHeap.offer(10);
        minHeap.offer(5);
        minHeap.offer(20);
        minHeap.offer(3);
        minHeap.offer(15);
        minHeap.offer(1);

        System.out.println("Heap after offers: " + minHeap); // Internal array representation

        System.out.println("Peek: " + minHeap.peek()); // Should be 1
        System.out.println("Size: " + minHeap.size()); // Should be 6

        System.out.println("Poll: " + minHeap.poll()); // Should be 1
        System.out.println("Heap after poll: " + minHeap);

        System.out.println("Poll: " + minHeap.poll()); // Should be 3
        System.out.println("Heap after poll: " + minHeap);

        minHeap.offer(2);
        System.out.println("Heap after offer(2): " + minHeap);
        System.out.println("Poll: " + minHeap.poll()); // Should be 2

        System.out.println("Is empty? " + minHeap.isEmpty());
        while (!minHeap.isEmpty()) {
            System.out.println("Polling: " + minHeap.poll());
        }
        System.out.println("Is empty? " + minHeap.isEmpty());

        try {
            minHeap.remove(); // Attempt to remove from empty queue
        } catch (NoSuchElementException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n--- Custom Ordering (Max-Heap using Comparator) ---");
        // Create a max-heap using a custom comparator
        MyPriorityQueue<Integer> maxHeap = new MyPriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(10);
        maxHeap.offer(5);
        maxHeap.offer(20);
        maxHeap.offer(3);
        maxHeap.offer(15);
        maxHeap.offer(1);

        System.out.println("Max Heap after offers: " + maxHeap);

        System.out.println("Peek (max): " + maxHeap.peek()); // Should be 20
        System.out.println("Poll (max): " + maxHeap.poll()); // Should be 20
        System.out.println("Max Heap after poll: " + maxHeap);

        System.out.println("\n--- Initializing with a Collection ---");
        List<String> names = new ArrayList<>();
        names.add("Charlie");
        names.add("Alice");
        names.add("Bob");
        names.add("David");

        MyPriorityQueue<String> nameMinHeap = new MyPriorityQueue<>(names);
        System.out.println("Name Min Heap: " + nameMinHeap);
        while (!nameMinHeap.isEmpty()) {
            System.out.println("Polling name: " + nameMinHeap.poll());
        }
    }
}