import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SimpleEchoServerClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 3333);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Server: " + input.readUTF());

            output.writeUTF("Hello from client");
            output.flush();
            System.out.println("Echo: " + input.readUTF());

            output.writeUTF("end");
            output.flush();
            System.out.println("Server: " + input.readUTF());
        }
    }
}
