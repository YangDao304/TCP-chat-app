package client;

import javax.swing.JTextArea;

public class JoinLeaveNotifier {

    public static void userJoined(
            JTextArea chatArea,
            String username) {

        chatArea.append(username + " joined the chat.\n");
    }

    public static void userLeft(
            JTextArea chatArea,
            String username) {

        chatArea.append(username + " left the chat.\n");
    }
}