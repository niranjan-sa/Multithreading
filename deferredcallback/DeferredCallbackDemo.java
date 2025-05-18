package deferredcallback;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.security.auth.callback.Callback;

public class DeferredCallbackDemo {
    public static void main(String[] args) {
        try {
            DeferredCallbackExecutor.runTestTenCallbacks();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class CallBack {
    long executeAt;
    String message;

    public CallBack(long executeAt, String message) {
        this.executeAt = System.currentTimeMillis() + executeAt;
        this.message = message;
    }
}

class DeferredCallbackExecutor {
    PriorityQueue<CallBack> queue = new PriorityQueue<CallBack>((a, b) -> {
        return (int)(a.executeAt - b.executeAt);
    });

    ReentrantLock lock = new ReentrantLock();
    Condition newCallbackArrived = lock.newCondition();

    public long findSleepDuration() {
        long sleepDuration = 0;
        sleepDuration = System.currentTimeMillis() - queue.peek().executeAt;
        return sleepDuration;
    }


    public void start() throws InterruptedException {
        long sleepfor = 0;
        while (true) { 
            lock.lock();

            while(queue.isEmpty()) {
                newCallbackArrived.await();
            }

            while(queue.size() != 0) {
                sleepfor = findSleepDuration();

                if(sleepfor <= 0) {
                    break;
                }
                
                newCallbackArrived.await(sleepfor, TimeUnit.MILLISECONDS);
            }

            CallBack callBack = queue.poll();
            System.out.println("Executing callback: " + callBack.message + 
                " at " + System.currentTimeMillis() + " with sleep duration: " + sleepfor);
            lock.unlock();
        }
    }


    public void registerCallBack(CallBack callBack) {
        lock.lock();
        try {
            queue.add(callBack);
            newCallbackArrived.signal();
        } finally {
            lock.unlock();
        }
    }


    public static void runTestTenCallbacks() throws InterruptedException { 
        Set<Thread> allThreads = new HashSet<Thread>(); 
        final DeferredCallbackExecutor deferredCallbackExecutor = new DeferredCallbackExecutor();
        Thread service = new Thread(new Runnable() { 
            public void run() { 
                try { 
                    deferredCallbackExecutor.start(); 
                } catch (InterruptedException ie) { 
                } 
            } 
        }); 

        service.start(); 
        for (int i = 0; i < 10; i++) { 
            Thread thread = new Thread(new Runnable() { 
                public void run() { 
                    CallBack cb = new CallBack(1, "Hello this is " + Thread.currentThread().getName());
                    deferredCallbackExecutor.registerCallBack(cb); 
                } 
            }); 
            thread.setName("Thread_" + (i + 1)); 
            thread.start(); 
            allThreads.add(thread); 
            Thread.sleep(1000); 
        } 
        for (Thread t : allThreads) { 
            t.join();
        }
    }
}
