import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private JLabel notificationLabel;
    private String username;

    public ChatGUI(String username, String serverIP, String port) {
        this.username = username;

        setTitle("Chat Room");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(245, 247, 250));

        JLabel userInfoLabel = new JLabel("User: " + username + " | Server: " + serverIP + ":" + port);
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel statusLabel = new JLabel("Status: Connected");
        statusLabel.setForeground(new Color(0, 150, 0));

        topPanel.add(userInfoLabel);
        topPanel.add(statusLabel);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createTitledBorder("Chat Messages"));

        DefaultListModel<String> userListModel = new DefaultListModel<>();
        userListModel.addElement(username);
        userListModel.addElement("User_1");
        userListModel.addElement("User_2");

        JList<String> onlineUserList = new JList<>(userListModel);
        onlineUserList.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane userScrollPane = new JScrollPane(onlineUserList);
        userScrollPane.setPreferredSize(new Dimension(160, 0));
        userScrollPane.setBorder(BorderFactory.createTitledBorder("Online Users"));

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel inputPanel = new JPanel(new BorderLayout(8, 8));
        inputPanel.setBackground(new Color(245, 247, 250));
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        notificationLabel = new JLabel("Welcome, " + username + "!");
        notificationLabel.setForeground(Color.DARK_GRAY);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.add(notificationLabel, BorderLayout.NORTH);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(chatScrollPane, BorderLayout.CENTER);
        mainPanel.add(userScrollPane, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        addSystemMessage("You joined the chat room.");

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();

        if (message.isEmpty()) {
            notificationLabel.setText("Notification: Please enter a message!");
            notificationLabel.setForeground(Color.RED);
            return;
        }

        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        chatArea.append("[" + time + "] " + username + ": " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());

        messageField.setText("");
        notificationLabel.setText("Message sent successfully.");
        notificationLabel.setForeground(new Color(0, 150, 0));
    }

    private void addSystemMessage(String message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        chatArea.append("[" + time + "] System: " + message + "\n");
    }
}
