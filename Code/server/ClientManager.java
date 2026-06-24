package server;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
public class ClientManager {

    private final List<ClientHandler> clients =
            new CopyOnWriteArrayList<>();

    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public ClientHandler findClient(String username) {

        for (ClientHandler client : clients) {

            if (client.getUsername() != null &&
                client.getUsername().equalsIgnoreCase(username)) {

                return client;
            }
        }

        return null;
    }

    public String getUserList() {

        StringBuilder sb = new StringBuilder();

        for (ClientHandler client : clients) {

            if (client.getUsername() != null) {

                if (sb.length() > 0) {
                    sb.append(", ");
                }

                sb.append(client.getUsername());
            }
        }

        return sb.toString();
    }
}