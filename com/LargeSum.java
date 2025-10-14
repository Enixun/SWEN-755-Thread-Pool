import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import src.ThreadManager;

public class LargeSum {

  public static void main(String[] args) {
    ThreadManager tm = new ThreadManager();
    tm.start();

    double[] lotsaNumbers = new double[100000000];
    for (int i = 0; i < lotsaNumbers.length; i++) {
      lotsaNumbers[i] = Math.random() * 10;
    }

    int step = 50000;
    AtomicLong sum = new AtomicLong(0);
    AtomicInteger numOps = new AtomicInteger(0);
    for (int j =0; j < lotsaNumbers.length; j += step) {
      final int start = j;
      try {
        tm.add(new Runnable() {
          @Override
          public synchronized void run() {
            int partialSum = 0;
            int end = Math.min(start + step, lotsaNumbers.length);
            // System.out.println("start " + start + "; end " + end);
            for (int k = start; k < end; k++) {
              partialSum += lotsaNumbers[k];
            }
            // System.out.println("Before: " + sum.get());
            // System.out.println("After: " + sum.addAndGet(partialSum)); 
            sum.addAndGet(partialSum);
            numOps.getAndIncrement();
            if (numOps.get() >= (int) lotsaNumbers.length / step) System.out.println("Final sum: " + sum);
          }
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    long longSum = 0;
    for (double num : lotsaNumbers) {
      longSum += num;
    }
    System.out.println("The long way: " + longSum);
    System.out.println("Length " + lotsaNumbers.length);
    System.out.println("Number of operations " + numOps + "; expected " + lotsaNumbers.length / step);
  }
}