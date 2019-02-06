package com.mygubbi.imaginestclientconnect.helpers;

import android.support.v4.app.Fragment;

import com.mygubbi.imaginestclientconnect.models.ClientProfile;

/**
 * @author Hemanth
 * @since 3/29/2018
 */
public interface OnFragmentInteractionListener {

    void onFragmentInteraction(String title, Fragment fragment);

    void onFragmentVisible();

    void onFragmentRemoved();

    void onProfileUpdated(ClientProfile clientProfile);

    void onBackClick();

}