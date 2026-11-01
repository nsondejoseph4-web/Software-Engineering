import java.io.*;
import java.net.*;

public class CliSocketConnection {
  public static void main(String[] args) throws Exception{
    Socket cliSocket;
    String host="localhost";

    cliSocket = new Socket(host,8421);
    System.out.println(" Connection with the server established, bye now!");
    cliSocket.close();
  }
}
