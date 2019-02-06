package com.mygubbi.imaginestclientconnect.clientAssistant;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientBaseView;
import com.mygubbi.imaginestclientconnect.models.ChatBotMessage;
import com.mygubbi.imaginestclientconnect.models.NotificationData;

import java.util.List;

interface ClientAssistantView extends ClientBaseView {

    void loginResult(boolean isSuccess);

    void showNotifications(List<NotificationData> notificationData);

    void showBotResponse(ChatBotMessage chatBotMessage);
}