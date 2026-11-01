import java.io.*;
import java.net.*;

public class SimpleJavaSocketServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1050);
        Socket cliSocket;
        System.out.println("Server is listening on port 1050..."); // print a status message in the servers terminal. This does not go to the client.
        cliSocket = serverSocket.accept();
        System.out.println("Connection with the client established!");
        new DataOutputStream(cliSocket.getOutputStream()).writeUTF("Hello client!");
        serverSocket.close();
        cliSocket.close();
    }
}