package DataStructuresImplementation;

import java.util.EmptyStackException; // Good practice for empty stack operations

public class ArrayStack<T> {
    private T[] stackArray;
    private int top; // Index of the top element
    private int capacity;

    // Constructor to initialize the stack with a given capacity
    public ArrayStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.capacity = capacity;
        this.stackArray = (T[]) new Object[capacity]; // Type casting required for generic array
        this.top = -1; // -1 indicates an empty stack
    }

    // Push an element onto the stack
    public void push(T element) {
        if (isFull()) {
            resize(); // Double the capacity if the stack is full
        }
        stackArray[++top] = element;
    }

    // Pop an element from the stack
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T element = stackArray[top];
        stackArray[top--] = null; // Avoid loitering (help garbage collection)
        return element;
    }

    // Peek at the top element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stackArray[top];
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return top == -1;
    }

    // Check if the stack is full (relevant for fixed-size array stacks)
    public boolean isFull() {
        return top == capacity - 1;
    }

    // Get the current size of the stack
    public int size() {
        return top + 1;
    }

    // Resize the underlying array when it's full
    private void resize() {
        capacity *= 2; // Double the capacity
        T[] newArray = (T[]) new Object[capacity];
        System.arraycopy(stackArray, 0, newArray, 0, top + 1);
        stackArray = newArray;
        System.out.println("Stack resized to: " + capacity); // For demonstration
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i <= top; i++) {
            sb.append(stackArray[i]);
            if (i < top) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        ArrayStack<Integer> myStack = new ArrayStack<>(3); // Initial capacity 3

        System.out.println("Is stack empty? " + myStack.isEmpty()); // Output: true

        myStack.push(10);
        myStack.push(20);
        myStack.push(30); // Stack is full here

        System.out.println("Stack: " + myStack); // Output: [10, 20, 30]
        System.out.println("Size: " + myStack.size()); // Output: 3

        myStack.push(40); // This will trigger resize
        System.out.println("Stack after push 40: " + myStack); // Output: [10, 20, 30, 40]
        System.out.println("Size: " + myStack.size()); // Output: 4

        System.out.println("Popped: " + myStack.pop()); // Output: 40
        System.out.println("Stack after pop: " + myStack); // Output: [10, 20, 30]

        System.out.println("Peek: " + myStack.peek()); // Output: 30
        System.out.println("Stack after peek: " + myStack); // Output: [10, 20, 30]

        while (!myStack.isEmpty()) {
            System.out.println("Popped: " + myStack.pop());
        }
        System.out.println("Is stack empty? " + myStack.isEmpty()); // Output: true

        try {
            myStack.pop(); // Trying to pop from empty stack
        } catch (EmptyStackException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
}