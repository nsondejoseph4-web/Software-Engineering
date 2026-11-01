import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        int port = 3333;
        System.out.println("Server started on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                     DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {
                    System.out.println("Connection with client established.");
                    output.writeUTF("Hello client, the connection with the Socket Server established!");
                    output.flush();

                    while (true) {
                        String message;
                        try {
                            message = input.readUTF();
                        } catch (EOFException e) {
                            break;
                        }

                        if ("end".equals(message)) {
                            System.out.println("From client: " + message);
                            break;
                        }

                        System.out.println("From client: " + message);
                        output.writeUTF(message);
                        output.flush();
                    }

                    System.out.println("Closing connection.");
                    output.writeUTF("Bye from Server");
                    output.flush();
                }
            }
        }
    }
}
