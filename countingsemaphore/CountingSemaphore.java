package countingsemaphore;
/*
 * Max permits are initially set
 * Unlike Java's semaphore, which has no upper bound. 
 * Can be released unlimited times. 
 * 
 * Here we have a max cap, and we block the thread if we reach the max cap.
 */
public class CountingSemaphore {
    private int usedPermits;
    private final int maxPermits;

    public CountingSemaphore(int maxPermits, int givenout) {
        this.maxPermits = maxPermits;
        this.usedPermits = maxPermits - givenout;
    }

    public synchronized void acquire() throws InterruptedException {
        while (usedPermits == maxPermits) {
            wait();
        }
        usedPermits++;
        notify();
    }

    public synchronized void release() throws InterruptedException {
        while(usedPermits == 0) {
            wait();
        }
        usedPermits--;
        notify();
    }
}
