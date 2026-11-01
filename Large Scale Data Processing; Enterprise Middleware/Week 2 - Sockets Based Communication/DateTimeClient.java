import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DateTimeClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 1313);
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(
                             socket.getInputStream(),
                             StandardCharsets.UTF_8))) {

            System.out.println("Server date and time: " + input.readLine());
        }
    }
}