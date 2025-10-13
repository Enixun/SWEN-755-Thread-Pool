package src;
import java.util.Random;

public class TestThread extends Thread {
  private static final Random RNG = new Random();
  @Override
  public void run() {
    System.out.println("Hello from " + Thread.currentThread().getName());
    try {
      Thread.sleep((RNG.nextInt(3) + 1) * 1000);
    } catch (InterruptedException ie) {}
    System.out.println("Finished from " + Thread.currentThread().getName());
  }
}