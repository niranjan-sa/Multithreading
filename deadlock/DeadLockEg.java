package deadlock;

public class DeadLockEg {
     public static void main(String args[]) { 
        Deadlock deadlock = new Deadlock(); 
        try { 
            deadlock.runTest(); 
        } catch (Exception ie) {

        } 
    } 
}


class Deadlock {
    private int counter;
    private Object lock1 = new Object();
    private Object lock2 = new Object();


    Runnable incrementer = new Runnable() {
        public void run() {

            for(int i = 0; i < 100; i++) {
                try {
                    incrementCounter();
                    System.out.println("Incrementing " + i); 
                } catch (Exception e) {
                }
            }
            
        }
    };

    Runnable decrementer = new Runnable() {
        public void run() {
            for(int i = 0; i < 100; i++) {
                try {
                    decrementCounter();
                    System.out.println("Decrementing " + i); 
                } catch (Exception e) {
                }
            }
            
        }
    };

    public void incrementCounter() throws InterruptedException{
        synchronized (lock1) {
            System.out.println("Lock1 acquired in increment");
            Thread.sleep(100);
            synchronized (lock2) {
                System.out.println("Locke 2 acquired in increment");
                counter++;
            }
        }
    }

    public void decrementCounter() throws InterruptedException{
        synchronized (lock2) {
            System.out.println("Lock2 acquired in decrement");
            Thread.sleep(100);
            synchronized (lock1) {
                System.out.println("Locke 2 acquired in decrement");
                counter--;
            }
        }
    }

    public void runTest() {
        Thread t1 = new Thread(incrementer);
        Thread t2 = new Thread(decrementer);

        t1.start(); t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
        }

        System.out.println("Done executing");
    }
}
