package com.mygubbi.imaginestclientconnect.models;

public class ClientHandover {

    private String item;
    private boolean isHeader;

    public ClientHandover(String item, boolean isHeader) {
        this.item = item;
        this.isHeader = isHeader;
    }

    public String getItem() {
        return item;
    }

    public boolean isHeader() {
        return isHeader;
    }

    @Override
    public String toString() {
        return "ClientHandover{" +
                "item='" + item + '\'' +
                ", isHeader=" + isHeader +
                '}';
    }
}