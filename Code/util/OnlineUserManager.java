package util;

import javax.swing.DefaultListModel;

public class OnlineUserManager {

    private final DefaultListModel<String> userListModel;

    public OnlineUserManager(DefaultListModel<String> userListModel) {
        this.userListModel = userListModel;
    }

    public void addUser(String username) {

        if (username == null || username.trim().isEmpty()) {
            return;
        }

        username = username.trim();

        if (!userListModel.contains(username)) {
            userListModel.addElement(username);
        }
    }

    public void removeUser(String username) {

        if (username == null || username.trim().isEmpty()) {
            return;
        }

        userListModel.removeElement(username.trim());
    }

    public void clearUsers() {
        userListModel.clear();
    }

    public boolean containsUser(String username) {

        if (username == null) {
            return false;
        }

        return userListModel.contains(username.trim());
    }

    public int getOnlineCount() {
        return userListModel.size();
    }

    public DefaultListModel<String> getUserListModel() {
        return userListModel;
    }

    public void processServerEvent(String message) {

        if (message == null || message.isEmpty()) {
            return;
        }

        /*
         * [SERVER] Online users: Hai, Tuan, Binh
         */
        if (message.contains("Online users:")) {

            clearUsers();

            String users =
                    message.substring(
                            message.indexOf("Online users:") +
                            "Online users:".length()).trim();

            if (!users.isEmpty()) {

                String[] userArray = users.split(",");

                for (String user : userArray) {
                    addUser(user.trim());
                }
            }
        }

        /*
         * [SERVER] Nam has joined the chat.
         */
        else if (message.contains("has joined the chat.")) {

            String username =
                    message.replace("[SERVER]", "")
                           .replace("has joined the chat.", "")
                           .trim();

            addUser(username);
        }

        /*
         * [SERVER] Hai has left the chat.
         */
        else if (message.contains("has left the chat.")) {

            String username =
                    message.replace("[SERVER]", "")
                           .replace("has left the chat.", "")
                           .trim();

            removeUser(username);
        }
    }
}