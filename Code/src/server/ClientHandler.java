package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
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
    }
}