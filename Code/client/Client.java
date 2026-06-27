package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;
    private static final int MAX_RETRIES = 3;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("--- Bat dau khoi dong TCP Client ---");

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

            ConnectionManager connectionManager = new ConnectionManager();
            boolean running = true;

            while (running) {
                try {
                    System.out.println("Dang ket noi Server tai " + SERVER_IP + ":" + SERVER_PORT + "...");
                    Socket socket = connectionManager.connect(SERVER_IP, SERVER_PORT);
                    System.out.println("Ket noi thanh cong toi Server!");

                    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                        out.println("USERNAME:" + username);
                        System.out.println("[Che do Chat - Nhap 'exit' de thoat, '/pm username message' de gui private message]");

                        Thread listener = new Thread(new MessageReceiver(in, message -> {
                            System.out.println(MessageReceiver.formatForDisplay(message));
                        }, () -> {
                            System.out.println("Ket noi da bi dong. Dang thu reconnect...");
                        }), "ServerListener");
                        listener.setDaemon(true);
                        listener.start();

                        while (true) {
                            System.out.print(username + ": ");
                            String message = scanner.nextLine();

                            if (message == null) {
                                continue;
                            }

                            String trimmed = message.trim();
                            if (trimmed.isEmpty()) {
                                continue;
                            }

                            if ("exit".equalsIgnoreCase(trimmed)) {
                                out.println(username + " da thoat.");
                                System.out.println("Dang ngat ket noi...");
                                break;
                            }

                            if (trimmed.startsWith("/pm ")) {
                                out.println(trimmed);
                                continue;
                            }

                            out.println(trimmed);
                        }
                    }

                    connectionManager.disconnect();
                    running = false;

                } catch (UnknownHostException e) {
                    System.err.println("Loi: Khong the tim thay IP may chu (" + SERVER_IP + ").");
                    break;
                } catch (ConnectException e) {
                    System.err.println("Loi ket noi: Server chua san sang. Thu lai sau 2 giay...");
                    sleep(2000);
                } catch (IOException e) {
                    System.err.println("Loi ket noi: " + e.getMessage());
                    sleep(2000);
                }
            }
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
