import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            }

        } catch (IOException e) {

            System.out.println("Server error!");
            e.printStackTrace();
        }
    }
}