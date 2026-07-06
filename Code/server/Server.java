package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.net.InetAddress;
import java.util.Scanner;

public class Server {


    public static void main(String[] args) {

        List<ClientHandler> clients =
                Collections.synchronizedList(new ArrayList<>());
        Scanner scanner = new Scanner(System.in);
        try {

            System.out.println("=================================");
            System.out.println("        TCP CHAT SERVER");
            System.out.println("=================================");

            System.out.print("Enter Port (Default 1234): ");

            String input = scanner.nextLine();

            int port;

            if (input.isBlank()) {
                port = 1234;
            } else {
                port = Integer.parseInt(input);
            }

            ServerSocket serverSocket = new ServerSocket(port);
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println();
            System.out.println("Server started successfully!");
            System.out.println("---------------------------------");
            System.out.println("Localhost : 127.0.0.1");
            System.out.println("Local IP  : " + ip.getHostAddress());
            System.out.println("Port      : " + port);
            System.out.println("---------------------------------");
            System.out.println("Waiting for clients...");
            System.out.println();

            while (true) {

                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, clients);
                clients.add(handler);
                new Thread(handler).start();

            }

        } catch (IOException e) {

            System.out.println("Server error!");
            e.printStackTrace();

        } catch (NumberFormatException e) {

            System.out.println("Invalid port number!");

        }

    }
}
