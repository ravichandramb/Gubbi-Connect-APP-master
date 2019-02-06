package com.mygubbi.imaginestclientconnect.clientConnectMain.fragments;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientBaseView;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;

/**
 * @author Hemanth
 * @since 4/6/2018
 */
public interface ClientConnectView extends ClientBaseView {

    void showProfileData(ClientProfile clientProfile);
}