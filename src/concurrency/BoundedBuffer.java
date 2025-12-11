package concurrency;

import java.util.concurrent.Semaphore;

public class BoundedBuffer {
    private final int[] buffer;
    private int head = 0, tail = 0, count = 0;
    private final int capacity;

    private final Semaphore mutex = new Semaphore(1); // mutual exclusion
    private final Semaphore empty; // empty slots
    private final Semaphore full;  // filled slots

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
        buffer = new int[capacity];
        empty = new Semaphore(capacity);
        full = new Semaphore(0);
    }

    // put value (producer)
    public void put(int value) throws InterruptedException {
        empty.acquire();
        mutex.acquire();
        try {
            buffer[tail] = value;
            tail = (tail + 1) % capacity;
            count++;
        } finally {
            mutex.release();
            full.release();
        }
    }

    // get value (consumer)
    public int get() throws InterruptedException {
        full.acquire();
        mutex.acquire();
        try {
            int val = buffer[head];
            head = (head + 1) % capacity;
            count--;
            return val;
        } finally {
            mutex.release();
            empty.release();
        }
    }

    public int size() {
        return count;
    }
}
