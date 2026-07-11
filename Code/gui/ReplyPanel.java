package gui;

import javax.swing.*;
import java.awt.*;

public class ReplyPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel contentLabel;

    private JButton closeButton;

    private MessageData message;

    public ReplyPanel() {

        setLayout(new BorderLayout());

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(180,180,180)),
                        BorderFactory.createEmptyBorder(
                                5,8,5,8)
                )
        );

        setBackground(new Color(245,245,245));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Reply");

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13));

        contentLabel = new JLabel("");

        contentLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12));

        textPanel.add(titleLabel);
        textPanel.add(contentLabel);

        closeButton = new JButton("✕");

        closeButton.setFocusable(false);

        closeButton.setMargin(
                new Insets(2,6,2,6));

        closeButton.addActionListener(e -> clearReply());

        add(textPanel,
                BorderLayout.CENTER);

        add(closeButton,
                BorderLayout.EAST);

        setVisible(false);

    }

    public void setReply(MessageData msg) {

        this.message = msg;

        titleLabel.setText(
                "Reply to "
                        + msg.getSender());

        String text =
                msg.getContent();

        if(text.length()>45){

            text =
                    text.substring(0,45)
                            + "...";

        }

        contentLabel.setText(text);

        setVisible(true);

    }

    public MessageData getReplyMessage(){

        return message;

    }

    public boolean hasReply(){

        return message!=null;

    }

    public void clearReply(){

        message=null;

        titleLabel.setText("Reply");

        contentLabel.setText("");

        setVisible(false);

    }

}
