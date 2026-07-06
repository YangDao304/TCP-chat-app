package util;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    private static boolean darkMode = false;
    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void toggleTheme(JFrame frame) {
        darkMode = !darkMode;
        applyTheme(frame);

    }

    public static void applyTheme(Component c) {
        Color background;
        Color foreground;
        Color buttonColor;

        if (darkMode) {
            background = new Color(40, 40, 40);
            foreground = Color.WHITE;
            buttonColor = new Color(70, 70, 70);
        } else {
            background = Color.WHITE;
            foreground = Color.BLACK;
            buttonColor = new Color(30, 144, 255);
        }
        update(c, background, foreground, buttonColor);
    }

    private static void update(
            Component c,
            Color bg,
            Color fg,
            Color btn) {

        if (c instanceof JPanel
                || c instanceof JScrollPane
                || c instanceof JTextArea
                || c instanceof JTextField
                || c instanceof JList) {
            c.setBackground(bg);
            c.setForeground(fg);
        }

        if (c instanceof JButton) {
            c.setBackground(btn);
            c.setForeground(Color.WHITE);
        }

        if (c instanceof JLabel) {
            c.setForeground(fg);
        }

        if (c instanceof Container) {
            for (Component child :
                    ((Container) c).getComponents()) {
                update(child, bg, fg, btn);
            }

        }

        if (c instanceof JScrollPane) {
            JScrollPane pane = (JScrollPane) c;
            if (pane.getBorder() instanceof javax.swing.border.TitledBorder title) {
                title.setTitleColor(fg);
            }

        }
        
    }

}