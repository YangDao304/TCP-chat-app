package gui;
import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JTextField serverIPField;
    private JTextField portField;
    private JLabel statusLabel;

    public LoginGUI() {
        setTitle("Client Login");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Chat Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 12, 12));
        formPanel.setBackground(new Color(245, 247, 250));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Server IP:"));
        serverIPField = new JTextField("127.0.0.1");
        formPanel.add(serverIPField);

        formPanel.add(new JLabel("Port:"));
        portField = new JTextField("8080");
        formPanel.add(portField);

        JButton connectButton = new JButton("Connect");
        connectButton.setFont(new Font("Arial", Font.BOLD, 14));

        statusLabel = new JLabel("Status: Not connected", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.add(connectButton);
        bottomPanel.add(statusLabel);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        connectButton.addActionListener(e -> connectToChat());
    }

    private void connectToChat() {
        String username = usernameField.getText().trim();
        String serverIP = serverIPField.getText().trim();
        String port = portField.getText().trim();

        if (username.isEmpty() || serverIP.isEmpty() || port.isEmpty()) {
            statusLabel.setText("Status: Please fill all fields");
            statusLabel.setForeground(Color.RED);
            return;
        }

        try {
            Integer.parseInt(port);
            statusLabel.setText("Status: Connected");
            statusLabel.setForeground(new Color(0, 150, 0));

        new ChatRoomGUI(username, serverIP, String.valueOf(port));
            dispose();

        } catch (NumberFormatException e) {
            statusLabel.setText("Status: Port must be a number");
            statusLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}
