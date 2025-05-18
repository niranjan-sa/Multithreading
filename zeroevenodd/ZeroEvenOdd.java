package zeroevenodd;

import java.util.concurrent.Semaphore;

class PrintStuff {
    Semaphore printZero;
    Semaphore printEven;
    Semaphore printOdd;

    int n;

    public PrintStuff(int n) {
        printZero = new Semaphore(1);
        printEven = new Semaphore(0);
        printOdd = new Semaphore(0);
        this.n = n;
    }

    public void printZero() throws InterruptedException{
        for(int i = 1; i < n; i++) {
            printZero.acquire();
            System.out.println(0);
            if(i%2 == 0) {
                printEven.release();
            } else {
                printOdd.release();
            }
        }
    }

    public void printEven() throws InterruptedException {
        for(int i = 2; i < n; i+=2) {
            printEven.acquire();
            System.out.println(i);
            printZero.release();
        }
    }

    public void printOdd() throws InterruptedException {
        for(int i = 1; i < n; i+=2) {
            printOdd.acquire();
            System.out.println(i);
            printZero.release();
        }
    }
}

public class ZeroEvenOdd {
    public static void main(String[] args) {
        PrintStuff ps = new PrintStuff(100);
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    ps.printZero();
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    ps.printEven();
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e);
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            public void run() {
                try {
                    ps.printOdd();
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e);
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
            // TODO: handle exception
        }
        System.out.println("Completed!");
    }
}
