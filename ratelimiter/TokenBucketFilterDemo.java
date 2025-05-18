package ratelimiter;
import java.util.*;
/*
 * Token Bucket Filter
 * Used for rate limiting.
 * 
 * Will have a producer thread that will produce tokens at a fixed rate.
 * And several consumer threads that will consume the tokens.
 * 
 * If tokens are not available, the consumer thread will block.
 */
public class TokenBucketFilterDemo {
    public static void main(String[] args) {
        try {
            TokenBucketFilterSimple.runTestMaxTokenIsTen();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class TokenBucketFilterSimple {
    long lastSeekTime;
    int MAX_TOKENS;
    int possibleTokens;


    public TokenBucketFilterSimple(int maxTokens) {
        this.MAX_TOKENS = maxTokens;
        this.lastSeekTime = System.currentTimeMillis();
        this.possibleTokens = 0;
    }

    public synchronized void getToken() throws InterruptedException {
        possibleTokens += (System.currentTimeMillis() - lastSeekTime) / 1000;
        if (possibleTokens > MAX_TOKENS) {
            possibleTokens = MAX_TOKENS;
        }

        if(possibleTokens  == 0) {
            System.out.println("No tokens available. Blocking thread.");
            Thread.sleep(1000);
        } else {
            possibleTokens -= 1;
        }
        lastSeekTime = System.currentTimeMillis();
        System.out.println("Consumed 1 token. Possible tokens: " + possibleTokens);
    }

    public static void runTestMaxTokenIsTen() throws InterruptedException { 
 
        Set<Thread> allThreads = new HashSet<Thread>(); 
        final TokenBucketFilterSimple tokenBucketFilter = new TokenBucketFilterSimple(5); 
 
        // Sleep for 10 seconds. 
        Thread.sleep(10000); 
 
        // Generate 12 threads requesting tokens almost all at once. 
        for (int i = 0; i < 12; i++) { 
 
            Thread thread = new Thread(new Runnable() { 
                public void run() { 
                    try { 
                        tokenBucketFilter.getToken();
                    } catch (InterruptedException ie) { 
                        System.out.println("We have a problem"); 
                    } 
                } 
            }); 
            thread.setName("Thread_" + (i + 1)); 
            allThreads.add(thread); 
        } 
 
        for (Thread t : allThreads) { 
            t.start(); 
        } 
 
        for (Thread t : allThreads) { 
            t.join(); 
        } 
    } 
}
