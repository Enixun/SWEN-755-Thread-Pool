import java.util.concurrent.atomic.AtomicInteger;

import src.ThreadManager;

public class LargeSum {

  public static void main(String[] args) {
    ThreadManager tm = new ThreadManager();
    tm.start(true);

    double[] lotsaNumbers = new double[1000000];
    for (int i = 0; i < lotsaNumbers.length; i++) {
      lotsaNumbers[i] = Math.random() * 10000;
    }

    int step = 200;
    AtomicInteger sum = new AtomicInteger(0);
    for (AtomicInteger j = new AtomicInteger(0); j.get() < lotsaNumbers.length; j.addAndGet(step)) {
      try {
        tm.add(new Runnable() {
          @Override
          public synchronized void run() {
            int partialSum = 0;
            int start = j.get();
            for (int k = start; k < Math.min(start + step, lotsaNumbers.length); k++) {
              partialSum += lotsaNumbers[k];
            }
            sum.addAndGet(partialSum); 
            if (start + step >= lotsaNumbers.length) System.out.println("Final sum: " + sum.get());
          }
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}