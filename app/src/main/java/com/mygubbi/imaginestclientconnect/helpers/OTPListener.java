package com.mygubbi.imaginestclientconnect.helpers;

public interface OTPListener {

    void onOTPEntered(String otp);

    void onOTPPasted();
}
