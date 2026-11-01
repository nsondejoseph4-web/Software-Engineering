
import java.io.*;
import java.net.*;

public class CliSocketReading{
  public static void main(String[] args) throws Exception{
    Socket cliSocket;
    DataInputStream is=null;
    String host="localhost";

    cliSocket = new Socket(host,8421);
    System.out.println(" Connection with the server established!");

    is = new DataInputStream(cliSocket.getInputStream());
    System.out.println(is.readUTF());
    cliSocket.close();
  }
}
