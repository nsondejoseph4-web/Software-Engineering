import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatServer {
    private static final int PORT = 4444;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server listening on port " + PORT + ". Waiting for a client...");

            try (Socket socket = serverSocket.accept();
                 BufferedReader incoming = new BufferedReader(
                         new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter outgoing = new PrintWriter(new OutputStreamWriter(
                         socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
                System.out.println("Client connected. Type messages; enter /quit to leave.");
                startKeyboardSender(socket, outgoing);
                receiveMessages(incoming);
            }
        }
    }

    private static void startKeyboardSender(Socket socket, PrintWriter outgoing) {
        Thread sender = new Thread(() -> {
            try {
                BufferedReader keyboard = new BufferedReader(
                        new InputStreamReader(System.in, StandardCharsets.UTF_8));
                String message;
                while ((message = keyboard.readLine()) != null) {
                    outgoing.println(message);
                    if ("/quit".equalsIgnoreCase(message)) {
                        socket.shutdownOutput();
                        return;
                    }
                }

                outgoing.println("/quit");
                socket.shutdownOutput();
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    System.err.println("Could not send chat message: " + e.getMessage());
                }
            }
        }, "chat-keyboard-sender");
        sender.setDaemon(true);
        sender.start();
    }

    private static void receiveMessages(BufferedReader incoming) throws IOException {
        String message;
        while ((message = incoming.readLine()) != null) {
            if ("/quit".equalsIgnoreCase(message)) {
                System.out.println("The client has left the chat.");
                return;
            }
            System.out.println("Client: " + message);
        }
        System.out.println("The client disconnected.");
    }
}
