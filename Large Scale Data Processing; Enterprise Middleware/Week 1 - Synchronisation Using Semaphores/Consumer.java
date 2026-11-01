import java.util.*;

public class Consumer extends Thread {
    private Semaphore sem;

    public Consumer(Semaphore t_sem) {
        sem = t_sem;
    }

    public void run() {
        System.out.println("CONSUMER STARTED");
        while(true) {
            Util.delay();
            sem.P(); // Lock the bank account
            
            // Check if there is at least £5 to withdraw to prevent an overdraft
            if (Data.balance - 5 >= 0) {
                System.out.println("Consumer: Reading balance = £" + Data.balance);
                Data.balance = Data.balance - 5;
                System.out.println("Consumer: Withdrew £5. New Balance = £" + Data.balance);
            } else {
                System.out.println("Consumer: Transaction Denied! Insufficient funds to withdraw £5.");
            }
            
            sem.V(); // Unlock the bank account
        }
    }
}

