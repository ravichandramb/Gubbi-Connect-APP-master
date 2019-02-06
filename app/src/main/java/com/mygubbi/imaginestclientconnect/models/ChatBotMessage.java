package com.mygubbi.imaginestclientconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hemanth
 * @since 5/09/2018
 */
public class ChatBotMessage {

    @SerializedName("answer")
    @Expose
    private String message;
    private boolean byUser;

    public ChatBotMessage(String message, boolean byUser) {
        this.message = message;
        this.byUser = byUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isByUser() {
        return byUser;
    }

    public void setByUser(boolean byUser) {
        this.byUser = byUser;
    }

}