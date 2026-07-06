package gui;

import util.ThemeManager;
import util.AvatarUtil;
import util.EmojiPicker;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatRoomGUI extends JFrame {

    private JPanel chatPanel;
    private JScrollPane chatScroll;

    private JTextField messageField;

    private DefaultListModel<String> userListModel;
    private JList<String> onlineUserList;

    private String selectedUser = "All";

    private PrintWriter out;
    private BufferedReader in;

    private String username;

    private JButton themeButton;
    private JButton emojiButton;
        public ChatRoomGUI(String username, String serverIP, String port) {

        this.username = username;

        setTitle("TCP Chat");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // CHAT PANEL (FIX GIÃN)
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBorder(null);

        chatScroll = new JScrollPane(chatPanel);
        chatScroll.getVerticalScrollBar().setUnitIncrement(10);
        add(chatScroll, BorderLayout.CENTER);
                userListModel = new DefaultListModel<>();
        userListModel.addElement("All");

        onlineUserList = new JList<>(userListModel);
        onlineUserList.setPreferredSize(new Dimension(170, 0));

        onlineUserList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedUser = onlineUserList.getSelectedValue();
            }
        });

        add(new JScrollPane(onlineUserList), BorderLayout.EAST);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));

        messageField = new JTextField();
        JButton send = new JButton("Send");

        emojiButton = new JButton("😊");

        themeButton = new JButton(
                ThemeManager.isDarkMode() ? "☀ Light Mode" : "🌙 Dark Mode"
        );

        JPanel buttons = new JPanel(new GridLayout(1, 3, 5, 5));
        buttons.add(emojiButton);
        buttons.add(send);
        buttons.add(themeButton);

        bottom.add(messageField, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
                send.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        emojiButton.addActionListener(
                e -> EmojiPicker.show(emojiButton, messageField)
        );

        themeButton.addActionListener(e -> {
            ThemeManager.toggleTheme(this);

            themeButton.setText(
                    ThemeManager.isDarkMode()
                            ? "☀ Light Mode"
                            : "🌙 Dark Mode"
            );
        });

        connect(serverIP, port);

        ThemeManager.applyTheme(this);

        setVisible(true);
    }
        private void connect(String ip, String port) {

        try {
            Socket socket = new Socket(ip, Integer.parseInt(port));

            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                    true
            );

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );

            out.println("USERNAME:" + username);

            new Thread(this::listen).start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server!");
            dispose();
        }
    }

    private void listen() {

        try {
            String msg;

            while ((msg = in.readLine()) != null) {

                if (msg.startsWith("USERLIST:")) {
                    updateUsers(msg.substring(9).split(","));
                } else {
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
        private String extractName(String msg) {

        try {
            int first = msg.indexOf("]");
            int colon = msg.indexOf(":");

            if (first != -1 && colon != -1) {
                return msg.substring(first + 2, colon).trim();
            }

        } catch (Exception ignored) {}

        return "system";
    }

    private void append(String msg) {

    SwingUtilities.invokeLater(() -> {

        String text = util.AvatarUtil.formatMessage(msg);
        String name = extractName(text);

        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));

        // 🔥 FIX: không cho nó nằm giữa
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel avatar = new JLabel(
                util.AvatarUtil.createAvatar(name, 28)
        );
        avatar.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        row.add(avatar);
        row.add(label);

        chatPanel.add(row);
        chatPanel.add(Box.createVerticalStrut(2));

        chatPanel.revalidate();
        chatPanel.repaint();

        JScrollBar bar = chatScroll.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
    });
}
    }
