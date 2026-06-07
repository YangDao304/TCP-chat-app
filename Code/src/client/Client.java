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
        System.out.println("--- Bat dau khoi dong TCP Client ---");
        System.out.println("Dang tim kiem Server tai " + SERVER_IP + ":" + SERVER_PORT + "...");

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Ket noi thanh cong toi Server!");
            System.out.println("\n[Che do Test Giao Tiep - Nhap 'exit' de thoat]");
            
            while (true) {
                System.out.print("Client (Ban): ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Dang ngat ket noi...");
                    break;
                }

                out.println(message);

                String response = in.readLine();
                if (response == null) {
                    System.out.println("Mat ket noi tu Server.");
                    break;
                }

               
                System.out.println(response);
            }

        } catch (UnknownHostException e) {
            System.err.println("Loi: Khong the tim thay IP may chu (" + SERVER_IP + ").");
        } catch (IOException e) {
            System.err.println("Loi ket noi: Server co the chua bat hoac sai Port.");
            System.err.println("Chi tiet loi: " + e.getMessage());
        }
    }
}