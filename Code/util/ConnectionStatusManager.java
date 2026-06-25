package util;

import javax.swing.JLabel;

public class ConnectionStatusManager {

    public static void setConnecting(JLabel label) {
        label.setText("Connecting...");
    }

    public static void setConnected(JLabel label) {
        label.setText("Connected");
    }

    public static void setDisconnected(JLabel label) {
        label.setText("Disconnected");
    }

    public static void setConnectionLost(JLabel label) {
        label.setText("Connection Lost");
    }

    public static void setReconnecting(JLabel label) {
        label.setText("Reconnecting...");
    }

    public static void setConnectionFailed(JLabel label) {
        label.setText("Connection Failed");
    }

    public static String getCurrentStatus(JLabel label) {
        return label.getText();
    }
}