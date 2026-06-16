package server;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    private Socket socket;
    private List<ClientHandler> clientList;
    private PrintWriter out;
    private String clientName;

    public ClientHandler(Socket socket, List<ClientHandler> clientList) {
        this.socket = socket;
        this.clientList = clientList;
        this.clientName = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public String getClientName() {
        return clientName;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            // Them client vao danh sach
            clientList.add(this);
            System.out.println("[+] " + clientName + " connected. Total clients: " + clientList.size());

            String message;

            while ((message = in.readLine()) != null) {

                System.out.println(clientName + ": " + message);

                out.println("Da nhan -> " + message);
            }

        } catch (Exception e) {
            System.out.println("Client disconnected: " + clientName);
        } finally {
            // Xoa client khoi danh sach khi ngat ket noi
            clientList.remove(this);
            System.out.println("[-] " + clientName + " removed. Total clients: " + clientList.size());

            try {
                socket.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}