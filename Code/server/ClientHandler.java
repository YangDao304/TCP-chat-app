package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

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
            sendMessage("[SERVER] Online users: " + getUserList());

            String message;
            while ((message = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(message.trim())) {
                    break;
                }

                processClientMessage(message);
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

        String formatted = username + ": " + message;
        appendInfo(formatted);
        broadcast(formatted, this);
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    private String getUserList() {
        StringBuilder builder = new StringBuilder();
        for (ClientHandler client : clients) {
            if (builder.length() > 0) {
                builder.append(", ");
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
