package com.mygubbi.imaginestclientconnect.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectActivity;
import com.mygubbi.imaginestclientconnect.clientIssues.activities.ClientIssueDetailActivity;

import java.util.Date;
import java.util.Map;

public class FCMService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FCMService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Log.d(TAG, "onMessageReceived: " + data.toString());

            sendNotification(data.get("title"), data.get("body"), data.get("notificationType"));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().toString());
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageTitle, String messageBody, String type) {
        Intent intent = new Intent(this, ClientConnectActivity.class);

        switch (type) {
            case "2":
                intent = new Intent(this, ClientConnectActivity.class);
                // Add extra intent here
                break;
            case "3":
                intent = new Intent(this, ClientIssueDetailActivity.class);
                // Add extra intent here
                break;
            case "4":
                intent = new Intent(this, ClientConnectActivity.class);
                break;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "GubbiConnect")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        if (notificationManager != null) {
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }
}
