package server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ClientManager manager;

    private BufferedReader in;
    private PrintWriter out;

    private String username;

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public ClientHandler(Socket socket, ClientManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );

            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                    true
            );

            // Nhận username từ client
            String firstLine = in.readLine();

            if (firstLine != null && firstLine.startsWith("USERNAME:")) {
                username = firstLine.substring(9).trim();
            }

            if (username == null || username.isEmpty()) {
                username = socket.getInetAddress().getHostAddress();
            }

            System.out.println(username + " connected");


            // Thông báo user mới
            manager.broadcast("[SERVER] " + username + " has joined chat.");
            sendUserList();

            sendMessage("[SERVER] Welcome " + username);


            // Nhận tin nhắn liên tục
            String msg;

            while ((msg = in.readLine()) != null) {

                if (msg.startsWith("/pm ")) {
                    handlePM(msg);
                } 
                else {
                    String time = LocalDateTime.now()
                            .format(TIME_FORMAT);

                    manager.broadcast(
                            "[" + time + "] "
                            + username + ": "
                            + msg
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + username);
        } 
        finally {
            cleanup();
        }
    }


    // Xử lý tin nhắn riêng
    private void handlePM(String msg) {

        String[] parts = msg.split(" ", 3);

        if (parts.length < 3) {
            return;
        }

        String target = parts[1];
        String content = parts[2];

        ClientHandler receiver = manager.findClient(target);

        String time = LocalDateTime.now()
                .format(TIME_FORMAT);


        if (receiver != null) {

            receiver.sendMessage(
                    "[PM][" + time + "] from "
                    + username + ": "
                    + content
            );

            sendMessage(
                    "[PM][" + time + "] to "
                    + target + ": "
                    + content
            );

        } else {
            sendMessage("[SERVER] User not found");
        }
    }


    public void sendMessage(String msg) {

        if (out != null) {
            out.println(msg);
        }
    }


    public String getUsername() {
        return username;
    }


    private void sendUserList() {

        manager.broadcast(
                "USERLIST:" + manager.getUserList()
        );
    }


    private void cleanup() {

        try {

            manager.removeClient(this);

            manager.broadcast(
                    "[SERVER] "
                    + username
                    + " left chat."
            );

            sendUserList();


            if (in != null)
                in.close();

            if (out != null)
                out.close();

            if (socket != null && !socket.isClosed())
                socket.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
