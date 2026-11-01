import java.util.*;

public class P1P2s {
    public static void main(String[] args) {
        // Initialize a binary semaphore (mutex lock) set to 1
        Semaphore sem = new Semaphore(1); 
        
        // Instantiate the new Producer and Consumer threads
        Producer producerThread = new Producer(sem);
        Consumer consumerThread = new Consumer(sem);
        
        // Fire up the threads
        producerThread.start();
        consumerThread.start();
    }
}

