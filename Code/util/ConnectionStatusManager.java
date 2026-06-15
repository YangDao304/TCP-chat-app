package util;

import javax.swing.JLabel;

public class ConnectionStatusManager {

    public static void setConnected(JLabel label) {

        label.setText("Connected");
    }

    public static void setDisconnected(JLabel label) {

        label.setText("Disconnected");
    }

    public static void setReconnecting(JLabel label) {

        label.setText("Reconnecting...");
    }

    public static void setConnectionFailed(JLabel label) {

        label.setText("Connection Failed");
    }
}