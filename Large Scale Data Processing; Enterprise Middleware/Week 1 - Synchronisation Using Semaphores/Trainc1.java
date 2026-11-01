import java.io.*;
public class Trainc1 {
  static FileWriter fw;
  static PrintWriter pw;


// The main controlling program in the 'train'
// system to demonstrate the use of semaphores.
 
  public static void main (String[] Args) throws java.io.IOException {
    int count;

    fw = new FileWriter("TrainOutput.txt");
    pw = new PrintWriter(fw);

    Semaphore[] sems = new Semaphore[2];
    Train1[] trains = new Train1[4];

    for (count = 0; count < 2; count++) {
      sems[count] = new Semaphore ();
    }

    for (count = 0; count <4; count++) {
      trains[count] = new Train1(count, sems, pw);
      trains[count].start();
    }

    // Wait until all trains reached their destination and then close the output file.
    boolean exit = false;
    while(!exit) {
      exit=true;
      for (count = 0; count <4; count++) {            
        if (trains[count].isAlive()) {
           exit=false;
           Util.delayr();
        }
      }
    }
    pw.close();
  }
}
