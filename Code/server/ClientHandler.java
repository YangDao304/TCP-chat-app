package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ClientManager manager;

    private BufferedReader in;
    private PrintWriter out;

    private String username;

    public ClientHandler(Socket socket, ClientManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(),
                    true);

            String remoteAddress =
                    socket.getInetAddress()
                          .getHostAddress();

            System.out.println(
                    "Connected from "
                    + remoteAddress
                    + ". Waiting for username...");

            String firstLine = in.readLine();

            if (firstLine == null) {
                return;
            }

            if (firstLine.startsWith("USERNAME:")) {

                username = firstLine
                        .substring("USERNAME:".length())
                        .trim();

            } else {

                username = remoteAddress;

                processMessage(firstLine);
            }

            if (username == null || username.isEmpty()) {
                username = remoteAddress;
            }

            String joinMessage =
                    username + " has joined the chat.";

            System.out.println(joinMessage);

            manager.broadcast(
                    "[SERVER] " + joinMessage);

            sendMessage(
                    "[SERVER] Welcome, "
                    + username
                    + "!");

            sendMessage(
                    "[SERVER] Online users: "
                    + manager.getUserList());

            String message;

            while ((message = in.readLine()) != null) {

                if ("exit".equalsIgnoreCase(
                        message.trim())
                    || "/exit".equalsIgnoreCase(
                        message.trim())) {

                    break;
                }

                processMessage(message);
            }

        } catch (IOException e) {

            System.out.println(
                    "Connection error for "
                    + username
                    + ": "
                    + e.getMessage());

        } finally {

            cleanup();
        }
    }

    private void processMessage(String message) {

        if (message == null ||
                message.trim().isEmpty()) {

            return;
        }

        // PRIVATE MESSAGE
        if (message.startsWith("/pm ")) {

            handlePrivateMessage(message);

            return;
        }

        String timestamp =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter
                                        .ofPattern(
                                                "HH:mm:ss"));

        String formatted =
                "[" + timestamp + "] "
                + username
                + ": "
                + message;

        System.out.println(formatted);

        manager.broadcast(formatted);
    }

    private void handlePrivateMessage(
            String command) {

        String[] parts =
                command.split(" ", 3);

        if (parts.length < 3) {

            sendMessage(
                    "[SERVER] Usage: "
                    + "/pm username message");

            return;
        }

        String targetUser = parts[1];
        String privateMessage = parts[2];

        ClientHandler receiver =
                manager.findClient(targetUser);

        if (receiver == null) {

            sendMessage(
                    "[SERVER] User "
                    + targetUser
                    + " not found.");

            return;
        }

        receiver.sendMessage(
                "[PM from "
                + username
                + "] "
                + privateMessage);

        sendMessage(
                "[PM to "
                + targetUser
                + "] "
                + privateMessage);

        System.out.println(
                "[PRIVATE] "
                + username
                + " -> "
                + targetUser
                + ": "
                + privateMessage);
    }

    public void sendMessage(String message) {

        if (out != null) {
            out.println(message);
        }
    }

    public String getUsername() {
        return username;
    }

    private void cleanup() {

        try {

            manager.removeClient(this);

            if (username != null) {

                String leaveMessage =
                        username
                        + " has left the chat.";

                System.out.println(leaveMessage);

                manager.broadcast(
                        "[SERVER] "
                        + leaveMessage);
            }

            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }

            if (socket != null &&
                    !socket.isClosed()) {

                socket.close();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}