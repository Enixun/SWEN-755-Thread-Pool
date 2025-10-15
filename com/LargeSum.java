import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import src.ThreadManager;

public class LargeSum {

  public static void main(String[] args) {
    ThreadManager tm = new ThreadManager();
    tm.start();

    long[] lotsaNumbers = new long[100000000];
    Random rand = new Random();
    for (int i = 0; i < lotsaNumbers.length; i++) {
      lotsaNumbers[i] = rand.nextLong();
    }

    int step = 50000;
    AtomicLong sum = new AtomicLong(0);
    CountDownLatch latch = new CountDownLatch((lotsaNumbers.length / step));
    for (int j = 0; j < lotsaNumbers.length; j += step) {
      final int start = j;
      try {
        tm.add(new Runnable() {
          @Override
          public void run() {
            long partialSum = 0;
            int end = Math.min(start + step, lotsaNumbers.length);
            // System.out.println("start " + start + "; end " + end);
            for (int k = start; k < end; k++) {
              partialSum += lotsaNumbers[k];
            }
            sum.addAndGet(partialSum);
            latch.countDown();
            // if (numOps.get() >= (int) lotsaNumbers.length / step)
            // System.out.println("Final sum: " + sum.get());
          }
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("The fast way: " + sum.get());

    long longSum = 0;
    for (long num : lotsaNumbers) {
      longSum += num;
    }
    System.out.println("The long way: " + longSum);
    System.out.println("Length " + lotsaNumbers.length);
  }
}
