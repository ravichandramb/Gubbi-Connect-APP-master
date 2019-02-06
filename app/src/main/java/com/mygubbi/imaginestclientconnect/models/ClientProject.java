package com.mygubbi.imaginestclientconnect.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hemanth
 * @since 3/28/2018
 */
public class ClientProject implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private int status;

    public ClientProject(String name, int status) {
        this.name = name;
        this.status = status;
    }

    private ClientProject(Parcel in) {
        name = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientProject> CREATOR = new Creator<ClientProject>() {
        @Override
        public ClientProject createFromParcel(Parcel in) {
            return new ClientProject(in);
        }

        @Override
        public ClientProject[] newArray(int size) {
            return new ClientProject[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}