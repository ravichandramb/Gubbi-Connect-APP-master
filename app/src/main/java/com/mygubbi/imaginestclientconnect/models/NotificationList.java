package com.mygubbi.imaginestclientconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 5/8/2018
 */
public class NotificationList {

    @SerializedName("userNotificationList")
    @Expose
    private ArrayList<NotificationData> userNotificationList = null;

    public ArrayList<NotificationData> getUserNotificationList() {
        return userNotificationList;
    }

    @Override
    public String toString() {
        return "NotificationList{" +
                "userNotificationList=" + userNotificationList +
                '}';
    }
}