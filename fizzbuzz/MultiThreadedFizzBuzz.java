package fizzbuzz;

class MultiThreadedFizzBuzzObj {
    int n;
    int num;

    public MultiThreadedFizzBuzzObj(int n) { 
        this.n = n;
        this.num = 1;
    }

    public synchronized void printFizz() throws InterruptedException {
        while(num <= n) {
            if(num % 3 == 0 && num % 5 != 0) {
                System.out.println("Fizz");
                num++;
                notifyAll();
            } else {
                wait();
            }
        }
    }

    public synchronized void printBuzz() throws InterruptedException{
        while(num <= n) {
            if(num % 3 != 0 && num % 5 == 0) {
                System.out.println("Buzz");
                num++;
                notifyAll();
            } else {
                wait();
            }
        }
    }

    public synchronized void printFizzBuzz() throws InterruptedException{
        while(num <= n) {
            if(num % 3 == 0 && num % 5 == 0) {
                System.out.println("FizzBuzz");
                num++;
                notifyAll();
            } else {
                wait();
            }
        }
    }

    public synchronized void printNumber() throws InterruptedException{
        while(num <= n) {
            if(num % 3 != 0 && num % 5 != 0) {
                System.out.println(num);
                num++;
                notifyAll();
            } else {
                wait();
            }
        }
    }
}

class FizzBuzzThread extends Thread {
    String action;
    MultiThreadedFizzBuzzObj obj;

    public FizzBuzzThread(String act, MultiThreadedFizzBuzzObj obj) {
        action = act;
        this.obj = obj;
    }

    public void run() {
        if(action.equals("fizz")) {
            try {
                obj.printFizz();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if(action.equals("buzz")) {
            try {
                obj.printBuzz();
            } catch(Exception e) {
                System.out.println(e);
            }
        } else if(action.equals("fizzbuzz")) {
            try {
                obj.printFizzBuzz();
            } catch(Exception e) {
                System.out.println(e);
            }
        } else {
            try {
                obj.printNumber();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

public class MultiThreadedFizzBuzz{
    public static void main(String[] args) {
        MultiThreadedFizzBuzzObj obj = new MultiThreadedFizzBuzzObj(100);
        FizzBuzzThread t1 = new FizzBuzzThread("fizz", obj);
        FizzBuzzThread t2 = new FizzBuzzThread("buzz", obj);
        FizzBuzzThread t3 = new FizzBuzzThread("fizzbuzz", obj);
        FizzBuzzThread t4 = new FizzBuzzThread("", obj);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch(Exception e) {
            System.out.println("Error joinng");
        }
    }
}