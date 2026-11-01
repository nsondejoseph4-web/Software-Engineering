import java.io.*;
public class Train1 extends Thread {

// This class simulates a train moving through
// from a yard into a critical section and
// out the other side

// Two trains exit each yard and another two wish
// to enter each yard.
	
  int name;
  String Sec1Name;
  String Sec2Name="C-Z";
  Semaphore semA, semZ;
  PrintWriter pw;
	
  Train1 (int trainName,Semaphore[] sems,PrintWriter pwr) {
    pw = pwr;
    name = trainName;
    semA = sems[0];
    semZ = sems[1];
    
    if (name == 0 || name ==1)
      Sec1Name="A-C";
    else
     Sec1Name="B-C";     

  }	  

  public void run() {

  // Trains leave the yard and move through a critical section

    System.out.println ("Train " + name + " ready to enter section "+Sec1Name);
    pw.println ("Train " + name + " ready to enter section "+Sec1Name+" at: "+Util.getTimeStamp());
    
    semA.P(); // Locks section C
    
      			
    System.out.println ("Train " + name + " entering section "+Sec1Name+" at: "+Util.getTimeStamp());
    pw.println ("Train " + name + " entering section "+Sec1Name+" at: "+Util.getTimeStamp());
    
    Util.delayr();
    
    System.out.println ("Train " + name + " Leaving section "+Sec1Name+" at: "+Util.getTimeStamp());
    pw.println ("Train " + name + " Leaving section "+Sec1Name+" at: "+Util.getTimeStamp());
    
    semA.V(); // Unlocks section C
    
    
    Util.delayr();

    semZ.P(); // Locks section Z
    
    
    System.out.println ("Train " + name + " entering section "+Sec2Name+" at: "+Util.getTimeStamp());
    pw.println ("Train " + name + " entering section "+Sec2Name+" at: "+Util.getTimeStamp());
    
    Util.delayr();
        			
    System.out.println ("Train " + name + " Leaving Section "+Sec2Name+" at: "+Util.getTimeStamp());
    pw.println ("Train " + name + " Leaving Section "+Sec2Name+" at: "+Util.getTimeStamp());
    
    semZ.V(); // Unlocks section Z

    Util.delayr();
        
    System.out.println ("Train " + name + " Exit track clear"+" at: "+Util.getTimeStamp());
    pw.println ("Train " + name + " Exit track clear"+" at: "+Util.getTimeStamp());
      
  }
}
