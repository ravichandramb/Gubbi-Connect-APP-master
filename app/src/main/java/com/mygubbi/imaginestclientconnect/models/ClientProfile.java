package com.mygubbi.imaginestclientconnect.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hemanth
 * @since 4/5/2018
 */
public class ClientProfile implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;
    @SerializedName("feedbackSubmitted")
    @Expose
    private String feedbackSubmitted;
    @SerializedName("projectCompletionDate")
    @Expose
    private String projectCompletionDate;
    @SerializedName("salesName")
    @Expose
    private String salesName;
    @SerializedName("salesPhone")
    @Expose
    private String salesPhone;
    @SerializedName("salesStage")
    @Expose
    private String salesStage;
    @SerializedName("supervisorName")
    @Expose
    private String supervisorName;
    @SerializedName("supervisorPhone")
    @Expose
    private String supervisorPhone;
    @SerializedName("designerName")
    @Expose
    private String designerName;
    @SerializedName("designerPhone")
    @Expose
    private String designerPhone;
    @SerializedName("updates")
    @Expose
    private String updates;

    private ClientProfile(Parcel in) {
        id = in.readString();
        customerName = in.readString();
        customerPhone = in.readString();
        feedbackSubmitted = in.readString();
        projectCompletionDate = in.readString();
        salesName = in.readString();
        salesPhone = in.readString();
        salesStage = in.readString();
        supervisorName = in.readString();
        supervisorPhone = in.readString();
        designerName = in.readString();
        designerPhone = in.readString();
        updates = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(customerName);
        dest.writeString(customerPhone);
        dest.writeString(feedbackSubmitted);
        dest.writeString(projectCompletionDate);
        dest.writeString(salesName);
        dest.writeString(salesPhone);
        dest.writeString(salesStage);
        dest.writeString(supervisorName);
        dest.writeString(supervisorPhone);
        dest.writeString(designerName);
        dest.writeString(designerPhone);
        dest.writeString(updates);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientProfile> CREATOR = new Creator<ClientProfile>() {
        @Override
        public ClientProfile createFromParcel(Parcel in) {
            return new ClientProfile(in);
        }

        @Override
        public ClientProfile[] newArray(int size) {
            return new ClientProfile[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getFeedbackSubmitted() {
        return feedbackSubmitted;
    }

    public String getProjectCompletionDate() {
        return projectCompletionDate;
    }

    public String getSalesName() {
        return salesName;
    }

    public String getSalesPhone() {
        return salesPhone;
    }

    public String getSalesStage() {
        return salesStage;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getSupervisorPhone() {
        return supervisorPhone;
    }

    public String getDesignerName() {
        return designerName;
    }

    public String getDesignerPhone() {
        return designerPhone;
    }

    public String getUpdates() { return updates; }

    public void setFeedbackSubmitted(String feedbackSubmitted) {
        this.feedbackSubmitted = feedbackSubmitted;
    }
}