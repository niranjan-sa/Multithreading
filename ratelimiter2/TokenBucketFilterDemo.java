package ratelimiter2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TokenBucketFilterDemo {
    public static void main(String[] args) throws InterruptedException{
        TokenBucketFilter tokenBucketFilter = TokenBucketFactory.createTokenBucketFilter(5);
        ExecutorService executors = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executors.execute(() -> {
                try {
                    tokenBucketFilter.getToken();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executors.shutdown();
        executors.awaitTermination(1, java.util.concurrent.TimeUnit.DAYS);
    }
}


class TokenBucketFactory {
    public static TokenBucketFilter createTokenBucketFilter(int maxTokens) {
        TokenBucketFilter tokenBucketFilter = new TokenBucketFilter(maxTokens);
        Thread daemonThread = new Thread(() -> {
            tokenBucketFilter.daemonThreadWork();
        });
        daemonThread.setDaemon(true);
        daemonThread.start();
        System.out.println("Daemon thread started");
        return tokenBucketFilter;
    }
}

class TokenBucketFilter {
    private int MAX_TOKENS;
    private int possibleTokens;
    private final int ONE_SECOND = 1000;

    public TokenBucketFilter(int maxTokens) {
        this.MAX_TOKENS = maxTokens;
        this.possibleTokens = 0;
    }

    public void daemonThreadWork() {
        while(true) {
            synchronized (this) {
                if(possibleTokens < MAX_TOKENS) {
                    possibleTokens++;
                    System.out.println("Added token. Possible tokens: " + possibleTokens);
                }
                this.notifyAll();
            }
            try {
                Thread.sleep(ONE_SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getToken() throws InterruptedException {
        synchronized (this) {
            while(possibleTokens == 0) {
                System.out.println("No tokens available. Blocking thread.");
                this.wait();
            }
            possibleTokens--;   
        }
        System.out.println("Consumed 1 token. Possible tokens: " + possibleTokens + " in thread: " + Thread.currentThread().getName());
    }
}