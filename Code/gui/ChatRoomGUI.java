package gui;

import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatRoomGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;

    private DefaultListModel<String> userListModel;
    private JList<String> onlineUserList;

    private String selectedUser = "All";

    private PrintWriter out;
    private BufferedReader in;

    private String username;

    private JButton themeButton;

    public ChatRoomGUI(
            String username,
            String serverIP,
            String port) {

        this.username = username;

        setTitle("TCP Chat");
        setSize(900,550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14));

        add(
                new JScrollPane(chatArea),
                BorderLayout.CENTER);

        userListModel =
                new DefaultListModel<>();

        userListModel.addElement("All");

        onlineUserList =
                new JList<>(userListModel);

        onlineUserList.setPreferredSize(
                new Dimension(
                        160,
                        0));

        onlineUserList.addListSelectionListener(
                e -> {

                    if (!e.getValueIsAdjusting()) {

                        String user =
                                onlineUserList
                                        .getSelectedValue();

                        if (user != null) {
                            selectedUser = user;
                        }
                    }
                });

        add(
                new JScrollPane(
                        onlineUserList),
                BorderLayout.EAST);

        JPanel bottom =
                new JPanel(
                        new BorderLayout());

        messageField =
                new JTextField();

        JButton send =
                new JButton("Send");

        themeButton =
                new JButton(
                        ThemeManager.isDarkMode()
                                ? "☀ Light Mode"
                                : "🌙 Dark Mode");

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                5,
                                5));

        buttonPanel.add(send);
        buttonPanel.add(themeButton);

        bottom.add(
                messageField,
                BorderLayout.CENTER);

        bottom.add(
                buttonPanel,
                BorderLayout.EAST);

        add(
                bottom,
                BorderLayout.SOUTH);

        send.addActionListener(
                e -> sendMessage());

        messageField.addActionListener(
                e -> sendMessage());

        themeButton.addActionListener(
                e -> {

                    ThemeManager.toggleTheme(this);

                    if (ThemeManager.isDarkMode()) {
                        themeButton.setText(
                                "☀ Light Mode");
                    }
                    else {
                        themeButton.setText(
                                "🌙 Dark Mode");
                    }
                });

        connect(serverIP, port);

        ThemeManager.applyTheme(this);

        setVisible(true);
    }

    private void connect(
            String ip,
            String port) {

        try {

            Socket socket =
                    new Socket(
                            ip,
                            Integer.parseInt(port));

            out =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true);

            in =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            out.println(
                    "USERNAME:"
                    + username);

            new Thread(
                    this::listen)
                    .start();

        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void listen() {

        try {

            String msg;

            while ((msg =
                    in.readLine()) != null) {

                if (msg.startsWith(
                        "USERLIST:")) {

                    updateUsers(
                            msg.substring(9)
                                    .split(","));
                }
                else {
                    append(msg);
                }
            }

        }
        catch (Exception e) {

            append("[DISCONNECTED]");
        }
    }

    private void updateUsers(
            String[] users) {

        SwingUtilities.invokeLater(
                () -> {

                    userListModel.clear();

                    userListModel
                            .addElement("All");

                    for (String u : users) {

                        if (!u.trim().isEmpty()
                                && !u.equals(username)) {

                            userListModel
                                    .addElement(u);
                        }
                    }
                });
    }

    private void sendMessage() {

        String msg =
                messageField
                        .getText()
                        .trim();

        if (msg.isEmpty()) {
            return;
        }

        if (!selectedUser.equals("All")) {

            out.println(
                    "/pm "
                    + selectedUser
                    + " "
                    + msg);
        }
        else {

            out.println(msg);
        }

        messageField.setText("");
    }

    private void append(String msg) {

        SwingUtilities.invokeLater(
                () -> chatArea.append(
                        msg + "\n"));
    }

    public static void main(String[] args) {

        new ChatRoomGUI(
                "User",
                "127.0.0.1",
                "1234");
    }
}