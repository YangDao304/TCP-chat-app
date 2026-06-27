package client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ConnectionManager {

    private Socket socket;

    public Socket connect(String ip, int port)
            throws UnknownHostException,
            ConnectException,
            SocketException,
            IOException {

        disconnect();
        socket = new Socket(ip, port);
        socket.setSoTimeout(0);
        return socket;
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        socket = null;
    }

    public Socket reconnect(String ip, int port)
            throws IOException {
        disconnect();
        return connect(ip, port);
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return socket != null
                && socket.isConnected()
                && !socket.isClosed();
    }
}