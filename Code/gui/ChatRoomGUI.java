package gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;

    private DefaultListModel<String> userListModel;
    private JList<String> onlineUserList;

    private String selectedUser = "All";

    private PrintWriter out;
    private BufferedReader in;

    private String username;

    public ChatGUI(String username, String serverIP, String port) {

        this.username = username;

        setTitle("TCP Chat");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // CHAT
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // ONLINE USERS (RIGHT)
        userListModel = new DefaultListModel<>();
        userListModel.addElement("All");

        onlineUserList = new JList<>(userListModel);
        onlineUserList.setPreferredSize(new Dimension(160, 0));

        onlineUserList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String user = onlineUserList.getSelectedValue();
                if (user != null) selectedUser = user;
            }
        });

        add(new JScrollPane(onlineUserList), BorderLayout.EAST);

        // INPUT
        JPanel bottom = new JPanel(new BorderLayout());

        messageField = new JTextField();
        JButton send = new JButton("Send");

        bottom.add(messageField, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        send.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        connect(serverIP, port);

        setVisible(true);
    }

    private void connect(String ip, String port) {
        try {
            Socket socket = new Socket(ip, Integer.parseInt(port));

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("USERNAME:" + username);

            new Thread(this::listen).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            String msg;

            while ((msg = in.readLine()) != null) {

                if (msg.startsWith("USERLIST:")) {
                    updateUsers(msg.substring(9).split(","));
                }

                else if (msg.startsWith("[SERVER]")) {
                    append(msg);
                }

                else {
                    append(msg);
                }
            }

        } catch (Exception e) {
            append("[DISCONNECTED]");
        }
    }

    private void updateUsers(String[] users) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            userListModel.addElement("All");

            for (String u : users) {
                if (!u.trim().isEmpty() && !u.equals(username)) {
                    userListModel.addElement(u);
                }
            }
        });
    }

    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (msg.isEmpty()) return;

        if (!selectedUser.equals("All")) {
            out.println("/pm " + selectedUser + " " + msg);
        } else {
            out.println(msg);
        }

        messageField.setText("");
    }

    private void append(String msg) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(msg + "\n");
        });
    }

    public static void main(String[] args) {
        new ChatGUI("User", "127.0.0.1", "1234");
    }
}
