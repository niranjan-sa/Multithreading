package boundedstack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedStackDemo {
    public static void main(String[] args) {
        BoundedBlockingStack<Integer> stk = new BoundedBlockingStack(10);
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i < 100; i++) {
                        stk.push(i);
                        Thread.sleep(100);
                        System.out.println("Pushed " + i);
                    }
                } catch (Exception e) {
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i < 100; i++) {
                        System.out.println("Popped " + stk.pop());
                        Thread.sleep(5);
                    }
                } catch (Exception e) {
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i < 100; i++) {
                        System.out.println("Peek " + stk.peek());
                        Thread.sleep(2);
                    }
                } catch (Exception e) {
                }
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
        }
        System.out.println("Completed");
    }
}

class BoundedBlockingStack <E> {
    private final Object[] elements;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    private int top = -1;

    public BoundedBlockingStack(int capacity) {
        this.elements = new Object[capacity];
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    public void push(E element) throws InterruptedException {
        if(element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
        lock.lock();
        try {
            while(isFull()) {
                System.out.println("Stack is full, waiting to push " + element);
                notFull.await();
            }
            elements[++top] = element;
            notEmpty.signal();
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public E pop() throws InterruptedException {
        lock.lock();
        try {
            while(isEmpty()) {
                System.out.println("Stack is empty, waiting to pop");
                notEmpty.await();
            }
            E element = (E) elements[top--];
            elements[top + 1] = null; // Avoid memory leak
            notFull.signal();
            return element;
        }  finally {
            lock.unlock();
        }
    }
    public E peek() throws InterruptedException {
        lock.lock();
        try {
            while(isEmpty()) {
                System.out.println("Stack is empty, waiting to peek");
                notEmpty.await();
            }
            E element = (E) elements[top];
            return element;
        } finally {
            lock.unlock();
        }
    }

    private boolean isEmpty() {
        return top == -1;
    }

    private boolean isFull() {
        return top == elements.length - 1;
    }

    public int size() {
        return top + 1;
    }
}
