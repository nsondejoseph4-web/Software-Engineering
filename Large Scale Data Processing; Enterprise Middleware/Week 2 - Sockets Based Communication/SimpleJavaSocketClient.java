import java.io.*;
import java.net.*;

public class SimpleJavaSocketClient {
    public static void main(String[] args) throws Exception {
        Socket cliSocket;
        String host = "localhost";
        cliSocket = new Socket(host, 1050);
        System.out.println("Connection with the server established!");
        DataInputStream is = new DataInputStream(cliSocket.getInputStream());
        System.out.println(is.readUTF());
        cliSocket.close();
    }
}