import java.net.*;
import java.io.*;
class SimpleSocketServer {
  public static void main(String args[])
    throws java.io.IOException {
    DataOutputStream out;
    InputStreamReader isr;
    System.out.println("Server running...");
    ServerSocket srvSocket= new ServerSocket(8421);
    Socket cliSocket = srvSocket.accept();
    out = new DataOutputStream(cliSocket.getOutputStream());
    out.writeUTF("Hello client, the connection with the Socket Server established, bye now!");
    System.out.println("The Socket Server exiting...");
    srvSocket.close();
    cliSocket.close();
  }
}
