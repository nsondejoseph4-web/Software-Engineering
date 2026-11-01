import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SimpleEchoServerClient2 {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 3333);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Server: " + input.readUTF());
            System.out.println("Type messages to send to the server. Press Ctrl+D when finished.");

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                System.out.println("User input: " + userInput);
                output.writeUTF(userInput);
                output.flush();
                System.out.println("Server reply: " + input.readUTF());
            }

            output.writeUTF("end");
            output.flush();
            System.out.println("Server: " + input.readUTF());
        }
    }
}
