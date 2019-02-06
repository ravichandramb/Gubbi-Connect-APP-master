package com.mygubbi.imaginestclientconnect.clientContacts;

import android.support.annotation.Nullable;

/**
 * @author Hemanth
 * @since 4/18/2018
 */
public class ClientContact {

    private String title;
    private String name;
    private String phone;
    private String imageUrl;

    ClientContact(String title, String name, String phone, @Nullable String imageUrl) {
        this.title = title;
        this.name = name;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "ClientContact{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}