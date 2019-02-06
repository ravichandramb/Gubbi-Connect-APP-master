package com.mygubbi.imaginestclientconnect.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String name;
    private String email;
    private String photo;
    private String uid;
    private String phoneNo;

    public User() {
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        photo = in.readString();
        uid = in.readString();
        phoneNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(photo);
        dest.writeString(uid);
        dest.writeString(phoneNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                ", uid='" + uid + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
