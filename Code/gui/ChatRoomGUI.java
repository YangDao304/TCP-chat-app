package gui;

import util.AvatarUtil;
import util.EmojiPicker;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public class ChatRoomGUI extends JFrame {

    private JPanel chatPanel;
    private JScrollPane chatScroll;

    private JTextField messageField;

    private ReplyPanel replyPanel;

    private JButton sendButton;
    private JButton emojiButton;
    private JButton themeButton;

    private JLabel roomAvatar;
    private JLabel roomName;
    private JLabel myAvatar;
    


    private DefaultListModel<String> userListModel;
    private JList<String> onlineUserList;


    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    private final String username;

    private String currentChat = "All";
    private boolean privateMode = false;


    private final ArrayList<MessageData> publicHistory =
            new ArrayList<>();

    private final HashMap<String, ArrayList<MessageData>> privateHistory =
            new HashMap<>();


    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");



    public ChatRoomGUI(String username, String serverIP, String port) {

        this.username = username;

        initUI();
        connect(serverIP, port);

        ThemeManager.applyTheme(this);

        setVisible(true);
    }



    private void initUI() {

        setTitle("TCP Chat");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());



   // HEADER

JPanel header = new JPanel(
        new BorderLayout()
);

header.setBackground(Color.WHITE);


// Avatar + tên phòng/người chat bên trái

roomAvatar =
        new JLabel(
                AvatarUtil.createAvatar("All",40)
        );


roomName =
        new JLabel("TCP Chat Room");


roomName.setFont(
        new Font(
                "Arial",
                Font.BOLD,
                16
        )
);



JPanel leftHeader = new JPanel(
        new FlowLayout(
                FlowLayout.LEFT,
                10,
                8
        )
);


leftHeader.add(roomAvatar);
leftHeader.add(roomName);



// Avatar của mình bên phải

myAvatar =
        new JLabel(
                AvatarUtil.createAvatar(username,40)
        );


JPanel rightHeader = new JPanel(
        new FlowLayout(
                FlowLayout.RIGHT,
                10,
                8
        )
);


rightHeader.add(myAvatar);



// Gắn vào header

header.add(
        leftHeader,
        BorderLayout.WEST
);


header.add(
        rightHeader,
        BorderLayout.EAST
);


add(
        header,
        BorderLayout.NORTH
);

        chatPanel = new JPanel();

        chatPanel.setLayout(
                new BoxLayout(chatPanel, BoxLayout.Y_AXIS)
        );


        chatScroll = new JScrollPane(chatPanel);

        chatScroll.getVerticalScrollBar()
                .setUnitIncrement(12);


        add(chatScroll, BorderLayout.CENTER);




        // USER LIST

        userListModel = new DefaultListModel<>();

        userListModel.addElement("All");


        onlineUserList =
                new JList<>(userListModel);


        onlineUserList.setPreferredSize(
                new Dimension(180, 0)
        );


        onlineUserList.addListSelectionListener(e -> {

            if(e.getValueIsAdjusting())
                return;


            String user =
                    onlineUserList.getSelectedValue();


            if(user == null)
                return;


            currentChat = user;



            if(user.equals("All")) {

                privateMode = false;

                setTitle("TCP Chat Room");

                roomAvatar.setIcon(
                        AvatarUtil.createAvatar("All",40)
                );

                roomName.setText(
                        "TCP Chat Room"
                );


            } else {

                privateMode = true;

                setTitle(
                        "Private Chat - " + user
                );


                roomAvatar.setIcon(
                        AvatarUtil.createAvatar(user,40)
                );


                roomName.setText(user);
            }


            loadHistory();

        });



        add(
                new JScrollPane(onlineUserList),
                BorderLayout.EAST
        );



        // INPUT AREA


        JPanel bottom =
                new JPanel(new BorderLayout(5,5));


        messageField =
                new JTextField();


        replyPanel =
                new ReplyPanel();


        replyPanel.setVisible(false);


        sendButton =
                new JButton("Send");


        emojiButton =
                new JButton("😊");


        themeButton =
                new JButton(
                        ThemeManager.isDarkMode()
                        ? "☀ Light"
                        : "🌙 Dark"
                );



        JPanel buttons =
                new JPanel(
                        new GridLayout(1,3,5,5)
                );


        buttons.add(emojiButton);
        buttons.add(sendButton);
        buttons.add(themeButton);



        JPanel inputPanel =
                new JPanel(new BorderLayout());


        inputPanel.add(
                replyPanel,
                BorderLayout.NORTH
        );


        inputPanel.add(
                messageField,
                BorderLayout.CENTER
        );



        bottom.add(
                inputPanel,
                BorderLayout.CENTER
        );


        bottom.add(
                buttons,
                BorderLayout.EAST
        );


        add(
                bottom,
                BorderLayout.SOUTH
        );



        sendButton.addActionListener(
                e -> sendMessage()
        );


        messageField.addActionListener(
                e -> sendMessage()
        );


        emojiButton.addActionListener(
                e -> EmojiPicker.show(
                        emojiButton,
                        messageField
                )
        );



        themeButton.addActionListener(e -> {

            ThemeManager.toggleTheme(this);

            themeButton.setText(
                    ThemeManager.isDarkMode()
                    ? "☀ Light"
                    : "🌙 Dark"
            );

        });

    }
        private void connect(String ip, String port) {

        try {

            socket = new Socket(
                    ip,
                    Integer.parseInt(port)
            );


            out = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(),
                            StandardCharsets.UTF_8
                    ),
                    true
            );


            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8
                    )
            );


            out.println(
                    "USERNAME:" + username
            );


            new Thread(
                    this::listen
            ).start();



        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cannot connect to server!"
            );

            dispose();
        }
    }




    private void listen() {

        try {

            String msg;


            while ((msg = in.readLine()) != null) {


                if(msg.startsWith("USERLIST:")) {

                    updateUsers(
                            msg.substring(9).split(",")
                    );

                    continue;
                }



                if(msg.startsWith("[PM]")) {

                    receivePrivate(msg);

                    continue;
                }



               String sender =
        extractName(msg);


String time = "";

try {

    int a = msg.indexOf("[");
    int b = msg.indexOf("]");

    if(a != -1 && b != -1){

        time = msg.substring(
                a + 1,
                b
        );
    }

}catch(Exception ignored){}



String content = msg;


int colon =
        msg.indexOf(
                ":",
                msg.indexOf("]")
        );


if(colon != -1){

    content =
            msg.substring(colon + 1)
            .trim();

}



MessageData data =
        new MessageData(
                sender,
                "All",
                content,
                time,
                false
        );


publicHistory.add(data);

if(!privateMode){

    appendMessage(data);

}
            }



        } catch(Exception e) {


            SwingUtilities.invokeLater(() ->

                    JOptionPane.showMessageDialog(
                            this,
                            "Disconnected from server."
                    )
            );
        }
    }





    private void updateUsers(String[] users) {


        SwingUtilities.invokeLater(() -> {


            String oldSelected =
                    currentChat;


            userListModel.clear();


            userListModel.addElement("All");



            for(String u : users) {


                if(!u.trim().isEmpty()
                        && !u.equals(username)) {


                    userListModel.addElement(
                            u
                    );
                }
            }



            onlineUserList.setSelectedValue(
                    oldSelected,
                    true
            );


        });
    }





    private void sendMessage() {


        String msg =
                messageField.getText()
                        .trim();


        MessageData reply =
                replyPanel.getReplyMessage();



        if(msg.isEmpty())
            return;




        if(privateMode) {


            if(reply == null) {


                out.println(
                        "/pm "
                        + currentChat
                        + " "
                        + msg
                );


            } else {


                out.println(
        "/pm "
        + currentChat
        + " "
        + "[REPLY]|"
        + reply.getSender()
        + "|"
        + reply.getContent()
        + "|"
        + msg
);

            }




            MessageData data =
                    new MessageData(
                            username,
                            currentChat,
                            msg,
                            getCurrentTime(),
                            true
                    );



            if(reply != null) {

                data.setReply(true);

                data.setReplySender(
                        reply.getSender()
                );

                data.setReplyContent(
                        reply.getContent()
                );
            }




            privateHistory
                    .computeIfAbsent(
                            currentChat,
                            k -> new ArrayList<>()
                    )
                    .add(data);



            appendMessage(data);



        } else {


            out.println(msg);

        }




        replyPanel.clearReply();

        messageField.setText("");
    }





    private void receivePrivate(String msg) {


        try {


            if(msg.contains("from ")) {

    int start =
            msg.indexOf("from ") + 5;


    int end =
            msg.indexOf(":", start);


    String friend =
            msg.substring(start,end)
                    .trim();


    String content =
            msg.substring(end + 1)
                    .trim();


    String time = getCurrentTime();



    MessageData data =
            new MessageData(
                    friend,
                    username,
                    content,
                    time,
                    true
            );


    // xử lý reply
    if(content.startsWith("[REPLY]|")) {


        String[] parts =
                content.split("\\|",4);


        if(parts.length == 4) {

    data = new MessageData(
            friend,
            username,
            parts[3],
            time,
            true
    );

    data.setReply(true);

    data.setReplySender(
            parts[1]
    );

    data.setReplyContent(
            parts[2]
    );
}
    }



    privateHistory
            .computeIfAbsent(
                    friend,
                    k -> new ArrayList<>()
            )
            .add(data);



    if(privateMode
            && currentChat.equals(friend)) {


        appendMessage(data);
    }
}



        } catch(Exception e) {

            e.printStackTrace();
        }
    }





    private void appendMessage(MessageData data) {


        SwingUtilities.invokeLater(() -> {


            ChatBubble bubble =
                    new ChatBubble(
                            data,
                            username,
                            this,
                            replyPanel,
                            userListModel,
                            out
                    );



            chatPanel.add(bubble);

            chatPanel.add(
                    Box.createVerticalStrut(3)
            );



            chatPanel.revalidate();

            chatPanel.repaint();



            
SwingUtilities.invokeLater(() -> {

    JScrollBar bar =
            chatScroll.getVerticalScrollBar();

    bar.setValue(
            bar.getMaximum()
    );

});

        });
    }





    private void loadHistory() {


        SwingUtilities.invokeLater(() -> {


            chatPanel.removeAll();



            if(privateMode) {


                ArrayList<MessageData> list =
                        privateHistory.get(
                                currentChat
                        );



                if(list != null) {

                    for(MessageData m : list) {

                        appendMessage(m);
                    }
                }


            } else {


                for(MessageData m : publicHistory) {

                    appendMessage(m);
                }
            }



            chatPanel.revalidate();

            chatPanel.repaint();


        });
    }





    private String extractName(String msg) {


        if(msg.startsWith("[SERVER]"))
            return "";



        try {


            int last =
                    msg.lastIndexOf("]");


            int colon =
                    msg.indexOf(
                            ":",
                            last
                    );


            if(last != -1 && colon != -1) {


                return msg.substring(
                        last + 1,
                        colon
                ).trim();
            }



        } catch(Exception ignored){}



        return "";
    }





    private String getCurrentTime() {

        return LocalTime.now()
                .format(TIME_FORMAT);
    }





    @Override
    public void dispose() {


        try {


            if(in != null)
                in.close();


            if(out != null)
                out.close();



            if(socket != null
                    && !socket.isClosed()) {


                socket.close();
            }



        } catch(Exception ignored){}



        super.dispose();
    }

}
