package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private PrintWriter out;
    private BufferedReader in;

    public ChatGUI() {

        setTitle("Chat App");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        add(scroll, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);

        connect();

        sendButton.addActionListener(e -> send());
        inputField.addActionListener(e -> send());
    }

    private void connect() {
        try {
            Socket socket = new Socket("127.0.0.1", 1234);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            chatArea.append("Connected\n");

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        chatArea.append("[SERVER] " + msg + "\n");
                    }
                } catch (Exception e) {
                    chatArea.append("Disconnected\n");
                }
            }).start();

        } catch (Exception e) {
            chatArea.append("Cannot connect\n");
        }
    }

    private void send() {

        String message = inputField.getText();

        if (!message.isEmpty()) {
            chatArea.append("[BAN] " + message + "\n");

            out.println(message);
            inputField.setText("");
        }
    }

    public static void main(String[] args) {
        new ChatGUI();
    }
}