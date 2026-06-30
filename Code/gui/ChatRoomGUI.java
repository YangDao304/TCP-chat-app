package gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatRoomGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    private DefaultListModel<String> userListModel;
    private JList<String> onlineUserList;

    private String selectedUser = "All";
    private JLabel currentChatLabel;

    private PrintWriter out;
    private BufferedReader in;

    private String username;

    public ChatRoomGUI(String username, String serverIP, String port) {

        this.username = username;

        setTitle("TCP Chat");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        currentChatLabel = new JLabel("Chatting with: All");
        currentChatLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        // CHAT
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN,14));
        
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createTitledBorder("Chat Messages"));

        // ONLINE USERS (RIGHT)
        userListModel = new DefaultListModel<>();
        userListModel.addElement("All");

        onlineUserList = new JList<>(userListModel);
        JScrollPane userScroll = new JScrollPane(onlineUserList);
        userScroll.setPreferredSize(new Dimension(180,0));
        userScroll.setBorder(BorderFactory.createTitledBorder("Online Users"));

        onlineUserList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String user = onlineUserList.getSelectedValue();
                if (user != null) selectedUser = user;
                currentChatLabel.setText("Chatting with: " + selectedUser);
            }
        });

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(currentChatLabel, BorderLayout.NORTH);
        chatPanel.add(chatScroll, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(chatPanel, BorderLayout.CENTER);
        centerPanel.add(userScroll, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        // INPUT
        JPanel bottom = new JPanel(new BorderLayout());

        messageField = new JTextField();
        JButton send = new JButton("Send");

        bottom.add(messageField, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        send.addActionListener(e -> {
            System.out.println("BUTTON CLICKED");
            sendMessage();
        });

        messageField.addActionListener(e -> {
            System.out.println("ENTER PRESSED");
            sendMessage();
        });

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
                    if(msg.startsWith("[PM]")){
                        append(
                            "\n====================\n"
                            + msg +
                            "\n====================");
                }

                else {
                    append(msg);
                }
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
            selectedUser = "All";

            for (String u : users) {
                if (!u.trim().isEmpty() && !u.equals(username)) {
                    userListModel.addElement(u);
                }
            }
        });
    }

    private void sendMessage() {
        System.out.println("Sending message to " + selectedUser);
        String msg = messageField.getText().trim();
        if (msg.isEmpty()) return;

        if (!selectedUser.equals("All")) {
            out.println("/pm " + selectedUser + " " + msg);
        } else {
            out.println(msg);
        }

        messageField.setText("");
        messageField.requestFocus();
    }

    private void append(String msg) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(msg + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        new ChatRoomGUI("User", "127.0.0.1", "1234");
    }
}
