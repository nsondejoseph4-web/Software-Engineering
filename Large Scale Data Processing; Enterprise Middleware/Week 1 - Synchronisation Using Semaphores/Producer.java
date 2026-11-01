import java.util.*;

public class Producer extends Thread {
    private Semaphore sem;

    public Producer(Semaphore t_sem) {
        sem = t_sem;
    }

    public void run() {
        System.out.println("PRODUCER STARTED");
        while(true) {
            Util.delay();
            sem.P(); // Lock the bank account
            
            // Check if adding £1 stays within the £100 maximum limit
            if (Data.balance + 1 <= 100) {
                System.out.println("Producer: Reading balance = £" + Data.balance);
                Data.balance = Data.balance + 1;
                System.out.println("Producer: Deposited £1. New Balance = £" + Data.balance);
            } else {
                System.out.println("Producer: Transaction Denied! Account is full at max limit (£100).");
            }
            
            sem.V(); // Unlock the bank account
        }
    }
}

