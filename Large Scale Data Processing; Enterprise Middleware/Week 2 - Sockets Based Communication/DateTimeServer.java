import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class DateTimeServer {
    public static void main(String[] args) throws IOException {
        int port = 1313;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Daytime server listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter output = new PrintWriter(
                             new OutputStreamWriter(
                                     clientSocket.getOutputStream(),
                                     StandardCharsets.UTF_8),
                             true)) {

                    String dateTime = LocalDateTime.now().toString();
                    output.println(dateTime);
                    System.out.println("Sent to client: " + dateTime);
                }
            }
        }
    }
}