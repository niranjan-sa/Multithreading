public class Demonstration2 {
    public static void main(String[] args) throws Exception{
        SynchronousExecutor executor = new SynchronousExecutor(); 
        executor.asynchronousExecution(() -> { 
            System.out.println("I am done"); 
        }); 
        System.out.println("main thread exiting...");
    }
}

interface Callback {
    public void done();
}

class SynchronousExecutor extends Executor {
    @Override
    public void asynchronousExecution(Callback callback) throws InterruptedException {
        Object signal = new Object();
        final boolean []isDone = new boolean[1];

        Callback cb = new Callback() {
            @Override
            public  void done() {
                callback.done();
                synchronized (signal) {
                    signal.notify();
                    isDone[0] = true;
                }
            }
        };
        super.asynchronousExecution(cb);
        synchronized (signal) {
            while(!isDone[0]) {
                signal.wait();
            }
        }
    }
}

class Executor {
    public void asynchronousExecution(Callback callback) throws InterruptedException {
        Thread t = new Thread(() -> {
            // Do some useful work
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
            }
            callback.done();
        });
        t.start(); 
      }
}