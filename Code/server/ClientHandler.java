package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final List<ClientHandler> clients;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
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
                
            String remoteAddress = socket.getInetAddress().getHostAddress();
            appendInfo("Connected from " + remoteAddress + ". Waiting for username...");

            String firstLine = in.readLine();
            if (firstLine == null) {
                return;
            }

            if (firstLine.startsWith("USERNAME:")) {
                username = firstLine.substring("USERNAME:".length()).trim();
            } else {
                username = remoteAddress;
                processClientMessage(firstLine);
            }

            if (username.isEmpty()) {
                username = remoteAddress;
            }

            String joinMessage = username + " has joined the chat.";
            appendInfo(joinMessage);
            broadcast("[SERVER] " + joinMessage, this);
            sendMessage("[SERVER] Welcome, " + username + "!");
            broadcast("USERLIST:" + getUserList(), null);

            String message;
            while ((message = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(message.trim())) {
                    break;
                }

                if (message.startsWith("/pm ")) {
                    processPrivateMessage(message);
                } else {
                    processClientMessage(message);
                }
            }
        } catch (IOException e) {
            appendInfo("Connection error for " + username + ": " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void processClientMessage(String message) {
    if (message == null || message.trim().isEmpty()) {
        return;
    }  

    String timestamp =
            LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("HH:mm:ss"));

    String formatted =
            "[" + timestamp + "] " + username + ": " + message;

    appendInfo(formatted);
    broadcast(formatted, this);
    }

    private void processPrivateMessage(String message) {

        String[] parts = message.split(" ", 3);

        if (parts.length < 3) {
            sendMessage("[SERVER] Invalid private message.");
            return;
        }

        String targetUser = parts[1];
        String content = parts[2];

        ClientHandler receiver = null;

        for (ClientHandler client : clients) {

            if (client.username != null &&
                client.username.equalsIgnoreCase(targetUser)) {

                receiver = client;
                break;
            }
        }

        if (receiver == null) {
            sendMessage("[SERVER] User not found.");
            return;
        }

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        receiver.sendMessage("[PM][" + timestamp + "] From "+ username + ": " + content);

        sendMessage("[PM][" + timestamp + "] To "+ targetUser + ": " + content);
    }
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            
            client.sendMessage(message);
        }
    }

    private String getUserList() {
        StringBuilder builder = new StringBuilder();
        for (ClientHandler client : clients) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(client.username == null ? "Unknown" : client.username);
        }
        return builder.toString();
    }

    private void closeConnection() {
        try {
            if (username != null) {
                String leaveMessage = username + " has left the chat.";
                appendInfo(leaveMessage);
                broadcast("[SERVER] " + leaveMessage, this);
            }

            clients.remove(this);
            broadcast("USERLIST:" + getUserList(), null);

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    private void appendInfo(String message) {
        System.out.println(message);
    }
}
