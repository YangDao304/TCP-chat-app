package gui;

public class MessageParser {

    public static ChatMessage parse(String line) {

        try {
            int rightBracket = line.indexOf("]");
            String time = line.substring(1, rightBracket);
            String remain = line.substring(rightBracket + 1).trim();
            int colon = remain.indexOf(":");
            String sender = remain.substring(0, colon).trim();
            String content = remain.substring(colon + 1).trim();
            return new ChatMessage(time, sender, content);
        } catch (Exception e) {

            return null;

        }

    }

}