package src;
import java.util.concurrent.BlockingQueue;

public class PoolThread extends Thread {
    private BlockingQueue<Runnable> q;
    private boolean logging;

    public PoolThread(String name, BlockingQueue<Runnable> q) {
      this(name, q, false);
    }

    public PoolThread(String name, BlockingQueue<Runnable> q, boolean logging) {
      super(name);
      this.q = q;
      this.logging = logging;
    }

    @Override
    public synchronized void run() {
      if (logging) System.out.println("Running " + Thread.currentThread().getName());
      while (true) {
        Runnable r = null;
        try {
          r = q.take();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        notify();
        if (r == null) continue;
        if (logging) System.out.println("Starting work on " + Thread.currentThread().getName());
        r.run();
        if (logging) System.out.println("Freed " + Thread.currentThread().getName());
      }
    }
  }