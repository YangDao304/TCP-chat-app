package client;

import javax.swing.DefaultListModel;

public class OnlineUserManager {

    private DefaultListModel<String> userListModel;

    public OnlineUserManager(DefaultListModel<String> userListModel) {

        this.userListModel = userListModel;
    }

    public void addUser(String username) {

        if (!userListModel.contains(username)) {
            userListModel.addElement(username);
        }
    }

    public void removeUser(String username) {

        userListModel.removeElement(username);
    }

    public void clearUsers() {

        userListModel.clear();
    }

    public int getOnlineCount() {

        return userListModel.size();
    }
}