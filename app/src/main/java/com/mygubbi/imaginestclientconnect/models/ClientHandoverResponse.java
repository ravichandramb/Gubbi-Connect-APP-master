package com.mygubbi.imaginestclientconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientHandoverResponse {

    @SerializedName("room_c")
    @Expose
    private String roomC;
    @SerializedName("sub_type_c")
    @Expose
    private String subTypeC;

    public String getRoomC() {
        return roomC;
    }

    public void setRoomC(String roomC) {
        this.roomC = roomC;
    }

    public String getSubTypeC() {
        return subTypeC;
    }

    public void setSubTypeC(String subTypeC) {
        this.subTypeC = subTypeC;
    }
}