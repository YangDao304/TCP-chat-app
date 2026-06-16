package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("--- Bat dau khoi dong TCP Client ---");
            System.out.println("Dang ket noi Server tai " + SERVER_IP + ":" + SERVER_PORT + "...");

            String username = "Client";
            if (args.length > 0 && args[0] != null && !args[0].trim().isEmpty()) {
                username = args[0].trim();
            } else {
                System.out.print("Nhap username: ");
                String inputName = scanner.nextLine().trim();
                if (!inputName.isEmpty()) {
                    username = inputName;
                }
            }

            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.println("Ket noi thanh cong toi Server!");
                System.out.println("[Che do Chat - Nhap 'exit' de thoat]");

                Thread listener = new Thread(() -> {
                    try {
                        String serverMessage;
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println(serverMessage);
                        }
                    } catch (IOException e) {
                        System.out.println("Loi: Ket noi voi Server bi mat.");
                    }
                }, "ServerListener");
                listener.setDaemon(true);
                listener.start();

                while (true) {
                    System.out.print(username + ": ");
                    String message = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(message.trim())) {
                        out.println(username + " da thoat.");
                        System.out.println("Dang ngat ket noi...");
                        break;
                    }

                    if (message.trim().isEmpty()) {
                        continue;
                    }

                    out.println(username + ": " + message);
                }

            } catch (UnknownHostException e) {
                System.err.println("Loi: Khong the tim thay IP may chu (" + SERVER_IP + ").");
            } catch (IOException e) {
                System.err.println("Loi ket noi: Khong the ket noi toi Server.");
                System.err.println("Chi tiet loi: " + e.getMessage());
            }
        }
    }
}
