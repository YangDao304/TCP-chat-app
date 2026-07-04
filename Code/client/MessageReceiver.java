package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

public class MessageReceiver implements Runnable {

    public enum MessageType {
        CHAT,
        PRIVATE,
        SYSTEM,
        USER_LIST,
        UNKNOWN
    }

    private final BufferedReader reader;
    private final Consumer<String> messageHandler;
    private final Runnable disconnectHandler;

    public MessageReceiver(BufferedReader reader,
                           Consumer<String> messageHandler,
                           Runnable disconnectHandler) {
        this.reader = reader;
        this.messageHandler = messageHandler;
        this.disconnectHandler = disconnectHandler;
    }

    @Override
    public void run() {
        try {
            String serverMessage;
            while ((serverMessage = reader.readLine()) != null) {
                if (serverMessage.trim().isEmpty()) {
                    continue;
                }
                messageHandler.accept(serverMessage);
            }
        } catch (IOException e) {
            // Connection closed or interrupted.
        } finally {
            if (disconnectHandler != null) {
                disconnectHandler.run();
            }
        }
    }

    public static MessageType parseMessageType(String rawMessage) {
        if (rawMessage == null || rawMessage.trim().isEmpty()) {
            return MessageType.UNKNOWN;
        }

        String message = rawMessage.trim();

        if (message.startsWith("USERLIST:")) {
            return MessageType.USER_LIST;
        }

        if (message.startsWith("[PM]")) {
            return MessageType.PRIVATE;
        }

        if (message.startsWith("[SERVER]")) {
            return MessageType.SYSTEM;
        }

        return MessageType.CHAT;
    }

    public static String formatForDisplay(String rawMessage) {
        if (rawMessage == null || rawMessage.trim().isEmpty()) {
            return "";
        }

        switch (parseMessageType(rawMessage)) {
            case PRIVATE:
                return "[PRIVATE] " + rawMessage;
            case SYSTEM:
                return "[SYSTEM] " + rawMessage;
            case USER_LIST:
                return "[ONLINE USERS] " + rawMessage.substring("USERLIST:".length());
            case CHAT:
            default:
                return rawMessage;
        }
    }
}
