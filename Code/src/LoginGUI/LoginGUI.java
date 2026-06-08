import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField serverIPField;
    private JTextField portField;
    private JButton loginButton;

    public LoginGUI() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Server IP:"));
        serverIPField = new JTextField("127.0.0.1");
        panel.add(serverIPField);

        panel.add(new JLabel("Port:"));
        portField = new JTextField("8080");
        panel.add(portField);

        loginButton = new JButton("Login");
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String serverIP = serverIPField.getText();
            String port = portField.getText();

            JOptionPane.showMessageDialog(this,
                    "Username: " + username +
                    "\nServer IP: " + serverIP +
                    "\nPort: " + port);
        });
    }

    public static void main(String[] args) {
        new LoginGUI().setVisible(true);
    }
}
