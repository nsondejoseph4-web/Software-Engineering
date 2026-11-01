/*  Util.java */
import java.time.*;

public class Util {
  public static void delay() {
    try {
      Thread.sleep((int) (2000));
    }
    catch(InterruptedException e){ System.out.println("Interrupted");}    
  }

  public static void delayr() {
    try {
      Thread.sleep((int)((Math.random() * 1000) * 2));
    }
    catch(InterruptedException e){ System.out.println("Interrupted");}
  }

  public static long getTimeStamp() {
    Instant instant = Instant.now();
    long timeStampMillis = instant.toEpochMilli();
    return timeStampMillis;
  }
}// End of class
