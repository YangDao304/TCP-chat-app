package gui;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.*;
import java.awt.*;
import util.ThemeManager;



public class ChatRoomGUI extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton themeButton;
    private JLabel userInfoLabel;
    private JLabel statusLabel;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    private String username;
    private DefaultListModel<String> userListModel;
    private JList<String> onlineUserList;

    private String selectedUser = "All";

    private AvatarPanel avatarPanel;
    private JLabel replyLabel;
    private JButton cancelReply;
    private JPanel replyPanel;

    private String replyMessage = null;
    private String replySender = null;

    private String clickedLine = null;

    public ChatRoomGUI(Socket socket, String username, String serverIP, String port) {
        this.socket = socket;
        try {

            out = new PrintWriter(
                socket.getOutputStream(),
                true);
            out.println("USERNAME:" + username);

            in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Connection Error!");
        }
        this.username = username;

        setTitle("Chat Room");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userInfoLabel = new JLabel("User: " + username + " | Server: " + serverIP + ":" + port);
        avatarPanel = new AvatarPanel(username);
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        statusLabel = new JLabel("Status: Connected");
        statusLabel.setForeground(new Color(67,181,129));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel infoPanel = new JPanel(new GridLayout(2,1));

        infoPanel.add(userInfoLabel);
        infoPanel.add(statusLabel);

        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        left.add(avatarPanel, BorderLayout.CENTER);
        topPanel.add(left, BorderLayout.WEST);
        topPanel.add(infoPanel, BorderLayout.CENTER);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 15));
        chatArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat Messages"));

        userListModel = new DefaultListModel<>();
        userListModel.addElement("All");

        onlineUserList = new JList<>(userListModel);
        onlineUserList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String user = onlineUserList.getSelectedValue();
                if (user != null) {
                    selectedUser = user;
                }
            }
        });

        JScrollPane userScroll = new JScrollPane(onlineUserList);
        userScroll.setPreferredSize(new Dimension(170, 0));
        userScroll.setBorder(BorderFactory.createTitledBorder("Online Users"));

        messageField = new JTextField();
        replyLabel = new JLabel();
        replyLabel.setVisible(false);
        replyLabel.setOpaque(true);

        replyLabel.setBackground(new Color(235,242,255));
        replyLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180,200,255)),
                BorderFactory.createEmptyBorder(8,10,8,10)
        ));

        replyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        replyLabel.setForeground(new Color(40,60,120));
        messageField.setFont(new Font("Arial", Font.PLAIN, 15));

        sendButton = new JButton("Send");
        themeButton = new JButton("Dark Mode");
        themeButton.setFocusPainted(false);
        themeButton.setFont(new Font("Arial", Font.BOLD, 12));
        themeButton.setPreferredSize(new Dimension(110, 35));
        
        sendButton.setFont(new Font("Arial", Font.BOLD, 13));
        sendButton.setBackground(new Color(30, 144, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);

        JPanel bottomPanel = new JPanel(new BorderLayout(5,5));
        JPanel rightPanel = new JPanel(new GridLayout(1,2,5,0));
        JPanel inputPanel = new JPanel(new BorderLayout());
        
        cancelReply = new JButton("Cancel");
        cancelReply.setVisible(false);
        cancelReply.setFocusable(false);
        cancelReply.addActionListener(e -> {

            replyMessage = null;
            replySender = null;

            replyLabel.setVisible(false);
            replyLabel.setText("");

            cancelReply.setVisible(false);
            replyPanel.setVisible(false);

        });

        replyPanel = new JPanel(new BorderLayout());
        
        replyPanel.add(replyLabel, BorderLayout.CENTER);
        replyPanel.add(cancelReply, BorderLayout.EAST);
        replyPanel.setVisible(false);
        
        inputPanel.add(replyPanel, BorderLayout.NORTH);
        inputPanel.add(messageField, BorderLayout.CENTER);

        rightPanel = new JPanel(new GridLayout(1,2,5,0));
        rightPanel.add(sendButton);
        rightPanel.add(themeButton);
        
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(userScroll, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        themeButton.addActionListener(e -> {
            ThemeManager.toggleTheme(this);
            if (ThemeManager.isDarkMode()) {
                themeButton.setText("Light Mode");
            } else {
                themeButton.setText("Dark Mode");
            }

        });
        messageField.addActionListener(e -> sendMessage());
        

        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.startsWith("USERLIST:")) {updateUsers(msg.substring(9).split(","));
                    } else {
                        appendMessage(msg);
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                    }
                }
            } catch (Exception e) {
                appendMessage("[SYSTEM] Disconnected from server");
            }

        }).start();
        createPopupMenu();
        ThemeManager.applyTheme(this);
        setVisible(true);
        
    }

    private void sendMessage() {

        String message = messageField.getText().trim();

        if(replyMessage!=null){
            message = "[REPLY]" + replySender + "|" + replyMessage + "|" + message;
        }  

        if (message.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter a message!");

            return;
        }
        
        if (selectedUser.equals("All")) {
            out.println(message);
        } else {
            
            out.println("/pm "+ selectedUser+ " "+ message);

        }
        messageField.setText("");
        replyMessage = null;
        replySender = null;

        replyLabel.setText("");
        replyLabel.setVisible(false);

        cancelReply.setVisible(false);
        replyPanel.setVisible(false);
    }

    private void updateUsers(String[] users) {

        SwingUtilities.invokeLater(() -> {

            userListModel.clear();
            userListModel.addElement("All");

            for (String user : users) {

                if (!user.trim().isEmpty()
                        && !user.equals(username)) {

                    userListModel.addElement(user);
                }
            }

        });

    }

    private void createPopupMenu() {

        JPopupMenu menu = new JPopupMenu();
        JMenuItem replyItem = new JMenuItem("Reply");
        JMenuItem forwardItem = new JMenuItem("Forward");

        forwardItem.addActionListener(e -> {

            if (clickedLine != null) {

                forwardMessage(clickedLine);

            }

        });

        replyItem.addActionListener(e -> {

        if (clickedLine != null) {

            setReplyTo(clickedLine);

        }

        });

        menu.add(replyItem);
        menu.add(forwardItem);
        chatArea.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {

                    try {

                        int pos = chatArea.viewToModel2D(e.getPoint());
                        int line = chatArea.getLineOfOffset(pos);
                        int start = chatArea.getLineStartOffset(line);
                        int end = chatArea.getLineEndOffset(line);
                        clickedLine = chatArea.getText(start, end - start).trim();

                    } catch (Exception ex) {
                        clickedLine = null;
                    }
                }
            }
        });

        chatArea.setComponentPopupMenu(menu);
    }

    private void setReplyTo(String line) {

        ChatMessage msg = MessageParser.parse(line);

        if (msg == null)
            return;

        replySender = msg.sender;
        replyMessage = msg.content;

        replyLabel.setText("Replying to " + msg.sender + ": " + msg.content);
        replyLabel.setVisible(true);
        cancelReply.setVisible(true);
        replyPanel.setVisible(true);

    }

    private void forwardMessage(String line) {

        ChatMessage msg = MessageParser.parse(line);

        if (msg == null)
            return;

        String[] users = new String[userListModel.size()];

        for (int i = 0; i < userListModel.size(); i++) {

            users[i] = userListModel.get(i);

        }

        String receiver = (String) JOptionPane.showInputDialog(this,
                "Forward to:",
                "Forward Message",
                JOptionPane.PLAIN_MESSAGE,
                null,
                users,
                users[0]);

        if (receiver == null)
            return;

        String forwardText =
                "[FORWARD]"
                + msg.sender
                + "|"
                + msg.content;

        if (receiver.equals("All")) {

            out.println(forwardText);

        } else {

            out.println("/pm " + receiver + " " + forwardText);

        }

    }

    private void appendMessage(String msg) {

        if (msg.contains("[FORWARD]")) {

            int index = msg.indexOf("[FORWARD]");
            String header = msg.substring(0, index);
            String data = msg.substring(index + "[FORWARD]".length());
            String[] parts = data.split("\\|", 2);
            if (parts.length == 2) {

                chatArea.append(header + "\n");
                chatArea.append(">>>> Forwarded from " + parts[0] + "\n");
                chatArea.append("────────────────────\n");
                chatArea.append(parts[1] + "\n\n");

                return;
            }
        }

        if (msg.contains("[REPLY]")) {

            int index = msg.indexOf("[REPLY]");
            String header = msg.substring(0, index);
            String data = msg.substring(index + "[REPLY]".length());
            String[] parts = data.split("\\|",3);

            if(parts.length == 3){

                chatArea.append(header + "\n");
                chatArea.append("Reply to " + parts[0] + "\n");
                chatArea.append("\"" + parts[1] + "\"\n");
                chatArea.append("────────────────────\n");
                chatArea.append(parts[2] + "\n\n");

                return;
            }

        }

        chatArea.append(msg + "\n");
    }

}