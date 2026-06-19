import javax.swing.*;
import java.awt.*;

public class ChatRoomGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel userInfoLabel;
    private JLabel statusLabel;

    private String username;

    public ChatRoomGUI(String username, String serverIP, String port) {
        this.username = username;

        setTitle("Chat Room");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userInfoLabel = new JLabel("User: " + username + " | Server: " + serverIP + ":" + port);
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        statusLabel = new JLabel("Status: Connected");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(userInfoLabel);
        topPanel.add(statusLabel);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat Messages"));

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 13));
        sendButton.setBackground(new Color(30, 144, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();

        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message!");
            return;
        }

        chatArea.append(username + ": " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());

        messageField.setText("");
    }
}
