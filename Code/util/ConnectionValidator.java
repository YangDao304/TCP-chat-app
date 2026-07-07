package util;

import java.util.regex.Pattern;

public class ConnectionValidator {

    public static boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty();
    }

    public static boolean isValidIP(String ip) {

        String regex =
                "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

        return Pattern.matches(regex, ip);
    }

    public static boolean isValidPort(int port) {
        return port >= 1 && port <= 65535;
    }
}