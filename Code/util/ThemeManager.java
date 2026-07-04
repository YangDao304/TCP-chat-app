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

    public static void applyTheme(Component component) {

        Color bg;
        Color fg;
        Color textBg;
        Color buttonBg;

        if (darkMode) {
            bg = new Color(43,43,43);
            fg = Color.WHITE;
            textBg = new Color(60,63,65);
            buttonBg = new Color(70,70,70);
        } else {
            bg = new Color(245,247,250);
            fg = Color.BLACK;
            textBg = Color.WHITE;
            buttonBg = new Color(230,230,230);
        }

        apply(component, bg, fg, textBg, buttonBg);
    }

    private static void apply(
            Component c,
            Color bg,
            Color fg,
            Color textBg,
            Color buttonBg) {

        if (c instanceof JPanel ||
                c instanceof JScrollPane ||
                c instanceof JFrame) {
            c.setBackground(bg);
            c.setForeground(fg);
        }

        if (c instanceof JTextField ||
                c instanceof JTextArea ||
                c instanceof JList) {
            c.setBackground(textBg);
            c.setForeground(fg);
        }

        if (c instanceof JButton) {
            c.setBackground(buttonBg);
            c.setForeground(fg);
        }

        if (c instanceof JLabel) {
            c.setForeground(fg);
        }

        if (c instanceof Container container) {
            for (Component child : container.getComponents()) {
                apply(child, bg, fg, textBg, buttonBg);
            }
        }
    }
}