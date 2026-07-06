package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AvatarUtil {

    private static final Color[] COLORS = {
            new Color(52, 152, 219),
            new Color(155, 89, 182),
            new Color(46, 204, 113),
            new Color(241, 196, 15),
            new Color(231, 76, 60),
            new Color(26, 188, 156)
    };

    public static Color getColor(String username) {
        if (username == null) return COLORS[0];
        int index = Math.abs(username.hashCode()) % COLORS.length;
        return COLORS[index];
    }


    public static ImageIcon createAvatar(String username, int size) {

        BufferedImage img =
                new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = img.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(getColor(username));
        g.fillOval(0, 0, size, size);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, size / 2));

        String text = (username == null || username.isEmpty())
                ? "?"
                : String.valueOf(username.charAt(0)).toUpperCase();

        FontMetrics fm = g.getFontMetrics();

        g.drawString(text,
                (size - fm.stringWidth(text)) / 2,
                (size + fm.getAscent()) / 2 - 4);

        g.dispose();

        return new ImageIcon(img);
    }

    public static String formatMessage(String msg) {
        return msg;
    }
}
