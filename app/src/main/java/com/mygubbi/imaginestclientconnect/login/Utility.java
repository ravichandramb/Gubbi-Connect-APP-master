package com.mygubbi.imaginestclientconnect.login;

import android.widget.EditText;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;

import java.util.regex.Pattern;

public abstract class Utility {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    public static boolean isNullOrEmpty(EditText s) {
        if (s == null
                || s.getText() == null
                || s.getText().toString() == null
                || s.getText().toString().trim()
                .equals(ClientConnectConstants.STR_BLANK)) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(TextView s) {
        if (s == null
                || s.getText() == null
                || s.getText().toString() == null
                || s.getText().toString().trim()
                .equals(ClientConnectConstants.STR_BLANK)) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.toString().trim().equals(ClientConnectConstants.STR_BLANK)
                || s.equalsIgnoreCase(ClientConnectConstants.STR_NULL)) {
            return true;
        }
        return false;
    }

}
