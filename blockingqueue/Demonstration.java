import java.lang.*;


public class Demonstration {
    public static void main(String[] args) {
        BlockingQueueD<Integer> que = new BlockingQueueD<Integer>(10);
        Thread t1 = new Thread(() -> {
            try {
                for(int i = 0; i < 100; i++) {
                    que.enqueue(i);
                    Thread.sleep(100);
                    System.out.println("Enqueued " + i);
                }
            } catch (Exception e) {
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                for(int i = 0; i < 100; i++) {
                    System.out.println("Enqueued " );
                    que.enqueue(i);
                    Thread.sleep(1);
                }
            } catch (Exception e) {
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                for(int i = 0; i < 100; i++) {
                    System.out.println("Dequeued " + que.dequeue());
                    Thread.sleep(20);
                }
            } catch (Exception e) {
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("All threads finished execution");
    }
}

class BlockingQueueD<T> {
    private T[] elements;
    private int capacity;
    private int head;
    private int tail;
    private int count;

    Object lock = new Object();

    @SuppressWarnings("unchecked")
    public BlockingQueueD(int capacity) {
        this.capacity = capacity;
        this.elements = (T[])new Object[capacity];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }

    public void  enqueue(T element) throws InterruptedException {
        synchronized (lock) {
            while (isFull()) {
                System.out.println("Queue is full, waiting to enqueue " + element);
                lock.wait();
            }
            this.elements[tail] = element;
            tail++;
            if (tail == capacity) {
                tail = 0;
            }
            count++;
            lock.notifyAll();
        }
    }

    public T  dequeue() throws InterruptedException {
        T element = null;
        synchronized(lock) {
            while(isEmpty()) {
                System.out.println("Queue is empty, waiting to dequeue");
                lock.wait();
            }
            
            element = (T) this.elements[head];
            head++;
            if(head == capacity) {
                head = 0;
            }
            count--;
            lock.notifyAll();
        }
        return element;
    }

    private boolean isFull() {
        return count == capacity;
    }

    private boolean isEmpty() {
        return count == 0;
    }
}