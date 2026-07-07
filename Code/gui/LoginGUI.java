package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import client.ConnectionManager;
import util.ConnectionValidator;
import gui.ChatRoomGUI;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JTextField serverIPField;
    private JTextField portField;
    private JButton connectButton;

    public LoginGUI() {
        setTitle("Client Login");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Server IP:"));
        serverIPField = new JTextField("127.0.0.1");
        panel.add(serverIPField);

        panel.add(new JLabel("Port:"));
        portField = new JTextField("1234");
        panel.add(portField);

        connectButton = new JButton("Connect");
        panel.add(new JLabel());
        panel.add(connectButton);

        add(panel);

        connectButton.addActionListener(e -> connectToServer());
    }

    private void connectToServer() {
        String username = usernameField.getText();
        String serverIP = serverIPField.getText();
        String portText = portField.getText();

        if (!ConnectionValidator.isValidUsername(username)) {

            JOptionPane.showMessageDialog(
                this,
                "Username cannot be empty!");

            return;
        }

        try {
            int port = Integer.parseInt(portText);

            if (!ConnectionValidator.isValidIP(serverIP)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid IP Address!");

            return;
            }

            if (!ConnectionValidator.isValidPort(port)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid Port!");

            return;
            }

            ConnectionManager manager = new ConnectionManager();

            Socket socket = manager.connect(serverIP, port);

            JOptionPane.showMessageDialog(this,
                "Connected successfully!\nUsername: " + username);

            new ChatRoomGUI(socket, username, serverIP, portText);

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Port must be a number!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Connection failed!\nPlease check Server IP and Port.");
        }
    }

    public static void main(String[] args) {
        new LoginGUI().setVisible(true);
    }
}
