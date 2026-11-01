import java.io.*;
import java.net.*;

public class CliSocketWriting {
  public static void main(String[] args) throws Exception{
    Socket cliSocket;
    DataInputStream is=null;
    DataOutputStream out=null;
    BufferedReader stdIn=null;

    String host="localhost";

    cliSocket = new Socket(host,8421);
    System.out.println(" Connection with the server established!");

    is = new DataInputStream(cliSocket.getInputStream());
    out = new DataOutputStream(cliSocket.getOutputStream());
    stdIn=new BufferedReader(new InputStreamReader(System.in));
    String userInput;
    while((userInput=stdIn.readLine())!=null) {
      out.writeUTF(userInput);
    }
    System.out.println(is.readUTF());
    cliSocket.close();
  }
}
