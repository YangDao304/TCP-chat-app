package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessagePopup extends JPopupMenu {

    private JMenuItem replyItem;
    private JMenuItem forwardItem;
    private JMenuItem copyItem;

    public interface PopupListener {

        void onReply();

        void onForward();

        void onCopy();

    }

    public MessagePopup(PopupListener listener) {

        replyItem = new JMenuItem("↩ Reply");

        forwardItem = new JMenuItem("➡ Forward");

        copyItem = new JMenuItem("📋 Copy");

        add(replyItem);

        add(forwardItem);

        addSeparator();

        add(copyItem);

        replyItem.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        listener.onReply();

                    }

                });

        forwardItem.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        listener.onForward();

                    }

                });

        copyItem.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        listener.onCopy();

                    }

                });

    }

}
