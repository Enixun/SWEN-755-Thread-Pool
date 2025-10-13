package src;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadManager {
  private static final int POOL_CAPACITY = 10;
  private BlockingQueue<Runnable> q;

  public ThreadManager() {
    q = new LinkedBlockingQueue<Runnable>();
  }

  public synchronized void add(Runnable r) throws InterruptedException {
    q.put(r);
    notifyAll();
  }

  public void addAll(Collection<Runnable> rs) throws InterruptedException {
    for (Runnable r: rs) {
      q.put(r);
    }
    notifyAll();
  }

  public void start() {
    this.start(false);
  }

  public void start(boolean logging) {
    for (int i = 0; i < POOL_CAPACITY; i++) {
      new PoolThread("Thread " + i, q, logging).start();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ThreadManager tm = new ThreadManager();
    tm.start(true);

    for (int i = 0; i < 20; i++) {
      String init = String.format("Some time passes on Task %s...", i);
      String stop = String.format("Task %s is done", i);
      tm.add(new Runnable() {
        @Override
        public void run() {
          System.out.printf(init + "; Running on %s%n", Thread.currentThread().getName());
          int waitTime = (int) (Math.random() * 10) * 1000;
          try {
            Thread.sleep(waitTime);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.printf(stop + "; Finished work on %s after %sms%n", Thread.currentThread().getName(), waitTime);
        }
      });
    }
  }
}
