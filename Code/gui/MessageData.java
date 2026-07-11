package gui;

public class MessageData {

    private String sender;
    private String receiver;
    private String content;
    private String time;

    private boolean privateMessage;

    private boolean reply;

    private String replySender;
    private String replyContent;

    public MessageData(
            String sender,
            String receiver,
            String content,
            String time,
            boolean privateMessage) {

        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
        this.privateMessage = privateMessage;

    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public String getReplySender() {
        return replySender;
    }

    public void setReplySender(String replySender) {
        this.replySender = replySender;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

}
