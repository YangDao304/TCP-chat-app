package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 1234;

    private static final ClientManager manager =
            new ClientManager();

    public static void main(String[] args) {

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            System.out.println("=================================");
            System.out.println(" SERVER IS RUNNING...");
            System.out.println(" Port: " + PORT);
            System.out.println("=================================");

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println(
                        "New connection: "
                                + socket.getInetAddress()
                                        .getHostAddress());

                ClientHandler handler =
                        new ClientHandler(socket, manager);

                manager.addClient(handler);

                new Thread(handler).start();
            }

        } catch (IOException e) {

            System.out.println("Server error!");

            e.printStackTrace();
        }
    }
}