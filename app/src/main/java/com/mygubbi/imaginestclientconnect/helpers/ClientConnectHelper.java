package com.mygubbi.imaginestclientconnect.helpers;

import android.os.Environment;
import android.text.TextUtils;

import java.util.regex.Pattern;


public class ClientConnectHelper {

    private static final String TAG = "ClientConnectHelper";

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    public static String upperCaseFirst(String text) {
        if (TextUtils.isEmpty(text))
            return text;

        // Convert String to char array.
        char[] array = text.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }

    public static boolean isExternalCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}