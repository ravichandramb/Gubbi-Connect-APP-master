package com.mygubbi.imaginestclientconnect.models;

/**
 * @author Hemanth
 * @since 5/09/2018
 */
public class ClientAssist {

    private NotificationData notificationData;
    private ChatBotMessage chatBotMessage;
    private boolean isChat;

    public ClientAssist(NotificationData notificationData) {
        this.notificationData = notificationData;
        this.isChat = false;
    }

    public ClientAssist(ChatBotMessage chatBotMessage) {
        this.chatBotMessage = chatBotMessage;
        this.isChat = true;
    }

    public NotificationData getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(NotificationData notificationData) {
        this.notificationData = notificationData;
    }

    public ChatBotMessage getChatBotMessage() {
        return chatBotMessage;
    }

    public void setChatBotMessage(ChatBotMessage chatBotMessage) {
        this.chatBotMessage = chatBotMessage;
    }

    public boolean isChat() {
        return isChat;
    }

    public void setChat(boolean chat) {
        this.isChat = chat;
    }

}