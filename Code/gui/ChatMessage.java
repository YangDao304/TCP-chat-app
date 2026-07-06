package gui;

public class ChatMessage {

    public String time;
    public String sender;
    public String content;

    public ChatMessage(String time, String sender, String content) {
        this.time = time;
        this.sender = sender;
        this.content = content;
    }

}