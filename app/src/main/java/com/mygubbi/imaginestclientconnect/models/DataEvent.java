package com.mygubbi.imaginestclientconnect.models;

/**
 * @author Hemanth
 * @since 4/18/2018
 */
public class DataEvent {

    private final String KEY;
    private final String VALUE;

    public final static String QUERY_BOT = "query_bot";
    public final static String DOWNLOAD_STATUS = "download_status";
    public final static String DOWNLOAD_IN_PROGRESS = "download_in_progress";
    public final static String DOWNLOAD_SUCCESS = "download_success";
    public final static String DOWNLOAD_FAILED = "download_failed";
    public final static String DOWNLOAD_PAUSED = "download_paused";
    public final static String DOWNLOAD_COMPLETED = "download_completed";

    public DataEvent(String KEY, String VALUE) {
        this.KEY = KEY;
        this.VALUE = VALUE;
    }

    public String getKey() {
        return KEY;
    }

    public String getValue() {
        return VALUE;
    }
}