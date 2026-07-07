package gui;

import javax.swing.*;
import java.awt.*;

public class AvatarPanel extends JPanel {

    private final String letter;
    private final Color color;

    public AvatarPanel(String username) {

        letter = username.substring(0,1).toUpperCase();

        Color[] colors = {

                new Color(52,152,219),
                new Color(231,76,60),
                new Color(46,204,113),
                new Color(155,89,182),
                new Color(241,196,15),
                new Color(230,126,34)

        };

        color = colors[Math.abs(username.hashCode()) % colors.length];
        setPreferredSize(new Dimension(74,74));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(color);
        g2.fillOval(3,3,64,64);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial",Font.BOLD,30));
        FontMetrics fm = g2.getFontMetrics();
        int x = (70 - fm.stringWidth(letter))/2;
        int y = ((70 - fm.getHeight())/2) + fm.getAscent();
        g2.drawString(letter,x,y);

    }

}