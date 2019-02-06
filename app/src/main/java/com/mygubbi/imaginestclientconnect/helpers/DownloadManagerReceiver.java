package com.mygubbi.imaginestclientconnect.helpers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.widget.Toast;

import com.mygubbi.imaginestclientconnect.models.DataEvent;

import org.greenrobot.eventbus.EventBus;

@SuppressWarnings("UnusedReturnValue")
public class DownloadManagerReceiver extends BroadcastReceiver {

//    private static final String TAG = "DownloadManagerReceiver";

    private DownloadManager downloadManager;
    public boolean isRegistered;

    // Required to declare in manifest
    @SuppressWarnings("unused")
    public DownloadManagerReceiver() {
    }

    public DownloadManagerReceiver(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                    String downloadComplete = "Download complete";
                    Toast.makeText(context, downloadComplete, Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new DataEvent(DataEvent.DOWNLOAD_SUCCESS, downloadComplete));
                } else if (DownloadManager.STATUS_RUNNING == cursor.getInt(columnIndex)) {
                    EventBus.getDefault().post(new DataEvent(DataEvent.DOWNLOAD_IN_PROGRESS, "Download in progress"));
                } else if (DownloadManager.STATUS_FAILED == cursor.getInt(columnIndex)) {
                    String statusText = "Download Failed";
                    String reasonText = "";
                    int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                    int reason = cursor.getInt(columnReason);
                    switch (reason) {
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            reasonText = "Error cannot resume";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            reasonText = "Error device not found";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            reasonText = "Error file already exists";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            reasonText = "Error file error";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            reasonText = "Error http data error";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            reasonText = "Error insufficient space";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            reasonText = "Error too many redirects";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            reasonText = "Error unhandled http code";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            reasonText = "Error unknown";
                            break;
                    }
                    EventBus.getDefault().post(new DataEvent(DataEvent.DOWNLOAD_FAILED, statusText + ": " + reasonText));
                }
            }
        }
    }

    /**
     * register receiver
     *
     * @param context - Context
     * @param filter  - Intent Filter
     * @return see Context.registerReceiver(BroadcastReceiver,IntentFilter)
     */
    public Intent register(Context context, IntentFilter filter) {
        try {
            return !isRegistered
                    ? context.registerReceiver(this, filter)
                    : null;
        } finally {
            isRegistered = true;
        }
    }

    /**
     * unregister received
     *
     * @param context - context
     * @return true if was registered else false
     */
    public boolean unregister(Context context) {
        // additional work match on context before unregister
        // eg store weak ref in register then compare in unregister
        // if match same instance
        return isRegistered
                && unregisterInternal(context);
    }

    private boolean unregisterInternal(Context context) {
        context.unregisterReceiver(this);
        isRegistered = false;
        return true;
    }
}