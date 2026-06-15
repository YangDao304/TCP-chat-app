package server;

import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT = 1234;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("=================================");
            System.out.println(" SERVER IS RUNNING...");
            System.out.println(" Port: " + PORT);
            System.out.println("=================================");

            while (true) {

                Socket socket = serverSocket.accept();

                String clientIP =
                        socket.getInetAddress().getHostAddress();

                System.out.println(
                        "New client connected: " + clientIP);

                new Thread(() -> {
                    try {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));

                        PrintWriter out = new PrintWriter(
                                socket.getOutputStream(), true);

                        String message;

                        while ((message = in.readLine()) != null) {

                            System.out.println("Client: " + message);

                            
                            out.println("Da nhan -> " + message);
                        }

                    } catch (Exception e) {
                        System.out.println("Client disconnected");
                    }
                }).start();
            }

        } catch (IOException e) {

            System.out.println("Server error!");
            e.printStackTrace();
        }
    }
}
