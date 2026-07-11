package gui;

import util.AvatarUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.util.Collections;


public class ChatBubble extends JPanel {

    private final MessageData message;


    public ChatBubble(
            MessageData message,
            String username,
            JFrame parent,
            ReplyPanel replyPanel,
            DefaultListModel<String> userListModel,
            PrintWriter out) {


        this.message = message;


        boolean isMe =
                message.getSender()
                        .equals(username);



        setLayout(
                new BoxLayout(
                        this,
                        BoxLayout.X_AXIS
                )
        );


        setBorder(
                BorderFactory.createEmptyBorder(
                        5,8,5,8
                )
        );


        setOpaque(false);




        JLabel avatar =
                new JLabel(
                        AvatarUtil.createAvatar(
                                message.getSender(),
                                35
                        )
                );




        JPanel bubble =
                new JPanel();


        bubble.setLayout(
                new BoxLayout(
                        bubble,
                        BoxLayout.Y_AXIS
                )
        );


        bubble.setBackground(
                Color.WHITE
        );


        bubble.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210,210,210)
                        ),
                        BorderFactory.createEmptyBorder(
                                6,10,6,10
                        )
                )
        );




        JLabel name =
                new JLabel(
                        message.getSender()
                );


        name.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );


        bubble.add(name);



        JLabel time =
                new JLabel(
                        message.getTime()
                );


        time.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );


        time.setForeground(
                Color.GRAY
        );


        bubble.add(time);



        bubble.add(
                Box.createVerticalStrut(4)
        );




        // Reply

        if(message.isReply()) {


            JPanel replyBox =
                    new JPanel();


            replyBox.setLayout(
                    new BoxLayout(
                            replyBox,
                            BoxLayout.Y_AXIS
                    )
);


            replyBox.setBackground(
                    new Color(240,240,240)
            );


            replyBox.setBorder(
                    BorderFactory.createEmptyBorder(
                            4,6,4,6
                    )
            );



            JLabel replyName =
                    new JLabel(
                            message.getReplySender()
                    );


            replyName.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            11
                    )
            );



            JLabel replyText =
                    new JLabel(
                            "<html>"
                            + message.getReplyContent()
                            .replace("\n","<br>")
                            + "</html>"
                    );


            replyBox.add(replyName);

            replyBox.add(replyText);



            bubble.add(replyBox);



            bubble.add(
                    Box.createVerticalStrut(5)
            );
        }




        JLabel content =
                new JLabel(
                        "<html>"
                        + message.getContent()
                        .replace("\n","<br>")
                        + "</html>"
                );


        content.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );


        bubble.add(content);



        // QUAN TRỌNG:
        // bubble tự co theo nội dung

        bubble.setMaximumSize(
                bubble.getPreferredSize()
        );




        if(isMe){


            add(
                    Box.createHorizontalGlue()
            );


            add(bubble);


            add(
                    Box.createHorizontalStrut(8)
            );


            add(avatar);



        }else{


            add(avatar);


            add(
                    Box.createHorizontalStrut(8)
            );


            add(bubble);


            add(
                    Box.createHorizontalGlue()
            );

        }




        // popup

        MessagePopup popup =
                new MessagePopup(

                new MessagePopup.PopupListener(){


                    @Override
                    public void onReply(){

                        replyPanel.setReply(
                                message
                        );

                    }



                    @Override
                    public void onForward(){


                        ForwardDialog dialog =
                                new ForwardDialog(
                                        parent,
                                        Collections.list(
                                                userListModel.elements()
                                        )
                                );

dialog.setVisible(true);



                        String target =
                                dialog.getSelectedUser();



                        if(target == null)
                            return;



                        if(target.equals("All")){


                            out.println(
                                    message.getContent()
                            );


                        }else{


                            out.println(
                                    "/pm "
                                    + target
                                    + " "
                                    + message.getContent()
                            );

                        }

                    }



                    @Override
                    public void onCopy(){


                        Toolkit.getDefaultToolkit()
                                .getSystemClipboard()
                                .setContents(
                                        new StringSelection(
                                                message.getContent()
                                        ),
                                        null
                                );

                    }

                });



        MouseAdapter mouse =
                new MouseAdapter(){


                    private void show(MouseEvent e){

                        if(e.isPopupTrigger()){

                            popup.show(
                                    e.getComponent(),
                                    e.getX(),
                                    e.getY()
                            );
                        }

                    }



                    public void mousePressed(MouseEvent e){
                        show(e);
                    }



                    public void mouseReleased(MouseEvent e){
                        show(e);
                    }

                };



        addMouseListener(mouse);
        bubble.addMouseListener(mouse);
        content.addMouseListener(mouse);
        avatar.addMouseListener(mouse);

        setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        getPreferredSize().height
                )
        );

    }
}
