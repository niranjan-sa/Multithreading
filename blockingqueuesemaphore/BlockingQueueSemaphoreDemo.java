package blockingqueuesemaphore;
import countingsemaphore.CountingSemaphore;
/*
 * We'll use our own counting semaphore to implement a blocking queue.
 * We will ensure the max no of permits and block the threads,
 * if those are given out.
 */

public class BlockingQueueSemaphoreDemo {
    public static void main(String[] args) {
        System.out.println("BlockingQueueSemaphore");
        BlockingQueueSemaphore<Integer> queue = new BlockingQueueSemaphore<>(10);
    
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    queue.enqueue(i);
                    System.out.println("Enqueued " + i);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    Integer item = queue.dequeue();
                    System.out.println("Dequeued " + item);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("All threads finished execution");
    }
}

public class BlockingQueueSemaphore<T> {
    private T[] elements;
    private int capacity;
    private int head;
    private int tail;
    private int count;
    private CountingSemaphore semProducer;
    private CountingSemaphore semConsumer;
    private CountingSemaphore semMutex;

    public BlockingQueueSemaphore(int capacity) {
        this.capacity = capacity;
        this.elements = (T[]) new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        this.semProducer = new CountingSemaphore(capacity, capacity);
        this.semConsumer = new CountingSemaphore(capacity, 0);
        // This is a mutex semaphore, which will be used to protect the critical section
        this.semMutex = new CountingSemaphore(1,1);

    }

    public void enqueue(T element) throws InterruptedException {
        semProducer.acquire();
        semMutex.acquire();

        if(tail == capacity) {
            tail = 0;
        }
        elements[tail] = element;
        tail++;
        count++;
        semMutex.release(); 
        semConsumer.release();
    }

    public T dequeue() throws InterruptedException {
        T item = null;
        semConsumer.acquire();
        semMutex.acquire();
        if(head == capacity) {
            head = 0;
        }
        item = elements[head];
        elements[head] = null;
        head++;
        count--;
        semMutex.release();
        semProducer.release();
        return item;
    }

    private boolean isFull() {
        return count == capacity;
    }

    private boolean isEmpty() {
        return count == 0;
    }
}
