package util;

import javax.swing.*;
import java.awt.*;

public class EmojiPicker {

    private EmojiPicker() {
    }

    public static void show(
            JButton emojiButton,
            JTextField messageField) {

        JPopupMenu menu =
                new JPopupMenu();

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                4,
                                6,
                                5,
                                5));

        String[] emojis = {

                "😀","😁","😂","🤣","😊","😍",
                "😘","🥰","😎","🤔","😭","😡",
                "👍","👎","👏","🙏","❤️","💔",
                "🔥","🎉","😅","😢","🤩","😴"

        };

        for (String emoji : emojis) {

            JButton button =
                    new JButton(emoji);

            button.setFont(
                    new Font(
                            "Segoe UI Emoji",
                            Font.PLAIN,
                            18));

            button.setMargin(
                    new Insets(
                            2,
                            2,
                            2,
                            2));

            button.setFocusable(false);

            button.addActionListener(e -> {

                messageField.replaceSelection(emoji);

                messageField.requestFocus();

                menu.setVisible(false);

            });

            panel.add(button);
        }

        menu.add(panel);

        menu.show(
                emojiButton,
                0,
                emojiButton.getHeight());
    }
}
