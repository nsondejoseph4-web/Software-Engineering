/**
        Semaphores.java
        Implementation of P & V semaphore operations
*/

public final class Semaphore {
  private int count_ = 0;

  public Semaphore() {
    count_ = 1;
  }

  public Semaphore(int maxvalue){
  count_=maxvalue;
  }

  public synchronized void V() {
    ++count_;
    notify();
  }

  public synchronized void P() {
    while (count_  <= 0)
     try { wait();} catch (InterruptedException ex) {}
    --count_;
  }
}


