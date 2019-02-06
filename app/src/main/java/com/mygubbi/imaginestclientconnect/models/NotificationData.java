package com.mygubbi.imaginestclientconnect.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hemanth
 * @since 5/8/2018
 */
public class NotificationData implements Parcelable, Comparable<NotificationData> {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("notificationType")
    @Expose
    private int notificationType;
    @SerializedName("createdTs")
    @Expose
    private long createdTs;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;

    private NotificationData(Parcel in) {
        id = in.readInt();
        userId = in.readString();
        title = in.readString();
        body = in.readString();
        data = in.readString();
        notificationType = in.readInt();
        createdTs = in.readLong();
        imgUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userId);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(data);
        dest.writeInt(notificationType);
        dest.writeLong(createdTs);
        dest.writeString(imgUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationData> CREATOR = new Creator<NotificationData>() {
        @Override
        public NotificationData createFromParcel(Parcel in) {
            return new NotificationData(in);
        }

        @Override
        public NotificationData[] newArray(int size) {
            return new NotificationData[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getData() {
        return data;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public long getCreatedTs() {
        return createdTs;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return "NotificationData{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", data='" + data + '\'' +
                ", notificationType=" + notificationType +
                ", createdTs=" + createdTs +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    /**
     * @param data the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(@NonNull NotificationData data) {
        if (this.getId() == data.getId()) {
            return 0;
        } else {
            return this.getId() > data.getId() ? 1 : -1;
        }
    }
}