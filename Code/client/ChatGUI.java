package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton disconnectButton;
    private JLabel statusLabel;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private String username;
    private Thread receiverThread;

    public ChatGUI(Socket socket, String username) {
        this.socket = socket;
        this.username = username;

        setTitle("Chat App - " + username);
        setSize(500, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        statusLabel = new JLabel("Connecting to server...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat Messages"));

        inputField = new JTextField();
        sendButton = new JButton("Send");
        disconnectButton = new JButton("Disconnect");

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        bottomPanel.add(inputField, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new BorderLayout(8, 0));
        buttons.add(sendButton, BorderLayout.CENTER);
        buttons.add(disconnectButton, BorderLayout.EAST);
        bottomPanel.add(buttons, BorderLayout.EAST);

        add(statusLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        disconnectButton.addActionListener(e -> disconnect());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });

        initializeConnection();
        setVisible(true);
    }

    private void initializeConnection() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            appendStatus("Connected to server: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            appendChat("You are connected as " + username + ".");

            receiverThread = new Thread(this::receiveLoop, "ChatReceiver");
            receiverThread.setDaemon(true);
            receiverThread.start();

            setConnected(true);
        } catch (IOException e) {
            appendStatus("Connection error: " + e.getMessage());
            appendChat("Unable to start message receive loop.");
            setConnected(false);
        }
    }

    private void receiveLoop() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                appendChat(message);
            }
            appendStatus("Server disconnected.");
        } catch (IOException e) {
            appendStatus("Connection lost: " + e.getMessage());
        } finally {
            setConnected(false);
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (message.isEmpty() || out == null) {
            return;
        }

        String formatted = username + ": " + message;
        out.println(formatted);
        //appendChat("[Me] " + message);
        inputField.setText("");
    }

    private void disconnect() {
        if (socket == null || socket.isClosed()) {
            return;
        }

        setConnected(false);

        try {
            if (out != null) {
                out.println(username + " has disconnected.");
            }
        } catch (Exception ignored) {
        }

        try {
            socket.close();
        } catch (IOException e) {
            appendStatus("Error while disconnecting: " + e.getMessage());
        }
    }

    private void setConnected(boolean connected) {
        SwingUtilities.invokeLater(() -> {
            inputField.setEnabled(connected);
            sendButton.setEnabled(connected);
            disconnectButton.setEnabled(connected);
            if (!connected) {
                inputField.setText("");
            }
        });
    }

    private void appendChat(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    private void appendStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(status));
    }

    public static void main(String[] args) {
    String serverIP = JOptionPane.showInputDialog(
            null,
            "Enter Server IP:",
            "127.0.0.1");

    String username = JOptionPane.showInputDialog(
            null,
            "Enter Username:");

    try {
        Socket socket = new Socket(serverIP, 1234);
        new ChatGUI(socket, username);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(
                null,
                "Cannot connect to server: " + e.getMessage());
                }
}
}
