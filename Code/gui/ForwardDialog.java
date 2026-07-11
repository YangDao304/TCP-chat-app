package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ForwardDialog extends JDialog {

    private JList<String> userList;

    private DefaultListModel<String> model;

    private JButton forwardButton;
    private JButton cancelButton;

    private String selectedUser;

    public ForwardDialog(
            JFrame parent,
            List<String> users) {

        super(parent,
                "Forward Message",
                true);

        setSize(300,350);

        setLocationRelativeTo(parent);

        setLayout(new BorderLayout(8,8));

        model = new DefaultListModel<>();

        for(String u : users){

            model.addElement(u);

        }

        userList = new JList<>(model);

        userList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(userList),
                BorderLayout.CENTER);

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT));

        cancelButton =
                new JButton("Cancel");

        forwardButton =
                new JButton("Forward");

        bottom.add(cancelButton);
        bottom.add(forwardButton);

        add(bottom,
                BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> {

            selectedUser = null;

            dispose();

        });

        forwardButton.addActionListener(e -> {

            selectedUser =
                    userList.getSelectedValue();

            if(selectedUser==null){

                JOptionPane.showMessageDialog(

                        this,

                        "Please select a user."

                );

                return;

            }

            dispose();

        });

    }

    public String getSelectedUser(){

        return selectedUser;

    }

}
