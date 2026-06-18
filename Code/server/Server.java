package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Server {

    private static final int PORT = 1234;

    public static void main(String[] args) {
        List<ClientHandler> clients =
                Collections.synchronizedList(new ArrayList<>());

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("=================================");
            System.out.println(" SERVER IS RUNNING...");
            System.out.println(" Port: " + PORT);
            System.out.println("=================================");

            while (true) {
                Socket socket = serverSocket.accept();

                ClientHandler handler =
                        new ClientHandler(socket, clients);

                clients.add(handler);

                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.out.println("Server error!");
            e.printStackTrace();
        }
    }
}
