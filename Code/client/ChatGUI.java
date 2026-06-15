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

    public ChatGUI(Socket socket, String username) {

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

        try {

            out = new PrintWriter(
                socket.getOutputStream(), true);

            in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));

            chatArea.append(
                "Connected as " + username + "\n");

            new Thread(() -> {

                try {

                    String msg;

                    while ((msg = in.readLine()) != null) {

                        chatArea.append(
                            "[SERVER] " + msg + "\n");
                    }

                } catch (Exception e) {

                    chatArea.append("Disconnected\n");
                }

            }).start();

        } catch (Exception e) {

            chatArea.append("Connection Error\n");
        }

        sendButton.addActionListener(e -> send());
        inputField.addActionListener(e -> send());

        setVisible(true);

        }

    private void send() {

        String message = inputField.getText();

        if (!message.trim().isEmpty()) {
            chatArea.append("[BAN] " + message + "\n");

            out.println(message);
            inputField.setText("");
        }
    }

   
}
