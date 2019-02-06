package com.mygubbi.imaginestclientconnect.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hemanth
 * @since 3/27/2018
 */
public class ClientIssue implements Parcelable {

    @SerializedName("OpportunityId")
    @Expose
    private String opportunityId;
    @SerializedName("OpportunityName")
    @Expose
    private String opportunityName;
    @SerializedName("assigned_user_id")
    @Expose
    private String assignedUserId;
    @SerializedName("assigned_user_name")
    @Expose
    private String assignedUserName;
    @SerializedName("case_id")
    @Expose
    private String caseId;
    @SerializedName("case_number")
    @Expose
    private String caseNumber;
    @SerializedName("case_root_cause_c")
    @Expose
    private String caseRootCauseC;
    @SerializedName("client_stage")
    @Expose
    private String clientStage;
    @SerializedName("commentLog")
    @Expose
    private String commentLog;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_name")
    @Expose
    private String createdName;
    @SerializedName("date_entered")
    @Expose
    private String dateEntered;
    @SerializedName("document")
    @Expose
    private Object document;
    @SerializedName("document_url")
    @Expose
    private String documentUrl;
    @SerializedName("due_date_c")
    @Expose
    private String dueDateC;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("gross_amount")
    @Expose
    private String grossAmount;
    @SerializedName("gstn")
    @Expose
    private String gstn;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("issue_category_c")
    @Expose
    private String issueCategoryC;
    @SerializedName("issue_sub_category_c")
    @Expose
    private String issueSubCategoryC;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shortage_c")
    @Expose
    private String shortageC;
    @SerializedName("shortage_item_detail_c")
    @Expose
    private String shortageItemDetailC;
    @SerializedName("shortage_item_qty_c")
    @Expose
    private String shortageItemQtyC;
    @SerializedName("shortage_item_size_c")
    @Expose
    private String shortageItemSizeC;
    @SerializedName("shortage_item_vendor_c")
    @Expose
    private String shortageItemVendorC;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sub_type_c")
    @Expose
    private String subTypeC;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;

    protected ClientIssue(Parcel in) {
        opportunityId = in.readString();
        opportunityName = in.readString();
        assignedUserId = in.readString();
        assignedUserName = in.readString();
        caseId = in.readString();
        caseNumber = in.readString();
        caseRootCauseC = in.readString();
        clientStage = in.readString();
        commentLog = in.readString();
        createdBy = in.readString();
        createdName = in.readString();
        dateEntered = in.readString();
        documentUrl = in.readString();
        dueDateC = in.readString();
        firstName = in.readString();
        grossAmount = in.readString();
        gstn = in.readString();
        id = in.readString();
        issueCategoryC = in.readString();
        issueSubCategoryC = in.readString();
        lastName = in.readString();
        name = in.readString();
        shortageC = in.readString();
        shortageItemDetailC = in.readString();
        shortageItemQtyC = in.readString();
        shortageItemSizeC = in.readString();
        shortageItemVendorC = in.readString();
        status = in.readString();
        subTypeC = in.readString();
        type = in.readString();
        vendorName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(opportunityId);
        dest.writeString(opportunityName);
        dest.writeString(assignedUserId);
        dest.writeString(assignedUserName);
        dest.writeString(caseId);
        dest.writeString(caseNumber);
        dest.writeString(caseRootCauseC);
        dest.writeString(clientStage);
        dest.writeString(commentLog);
        dest.writeString(createdBy);
        dest.writeString(createdName);
        dest.writeString(dateEntered);
        dest.writeString(documentUrl);
        dest.writeString(dueDateC);
        dest.writeString(firstName);
        dest.writeString(grossAmount);
        dest.writeString(gstn);
        dest.writeString(id);
        dest.writeString(issueCategoryC);
        dest.writeString(issueSubCategoryC);
        dest.writeString(lastName);
        dest.writeString(name);
        dest.writeString(shortageC);
        dest.writeString(shortageItemDetailC);
        dest.writeString(shortageItemQtyC);
        dest.writeString(shortageItemSizeC);
        dest.writeString(shortageItemVendorC);
        dest.writeString(status);
        dest.writeString(subTypeC);
        dest.writeString(type);
        dest.writeString(vendorName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientIssue> CREATOR = new Creator<ClientIssue>() {
        @Override
        public ClientIssue createFromParcel(Parcel in) {
            return new ClientIssue(in);
        }

        @Override
        public ClientIssue[] newArray(int size) {
            return new ClientIssue[size];
        }
    };

    public String getOpportunityId() {
        return opportunityId;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public String getCaseRootCauseC() {
        return caseRootCauseC;
    }

    public String getClientStage() {
        return clientStage;
    }

    public String getCommentLog() {
        return commentLog;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedName() {
        return createdName;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public Object getDocument() {
        return document;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public String getDueDateC() {
        return dueDateC;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public String getGstn() {
        return gstn;
    }

    public String getId() {
        return id;
    }

    public String getIssueCategoryC() {
        return issueCategoryC;
    }

    public String getIssueSubCategoryC() {
        return issueSubCategoryC;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getShortageC() {
        return shortageC;
    }

    public String getShortageItemDetailC() {
        return shortageItemDetailC;
    }

    public String getShortageItemQtyC() {
        return shortageItemQtyC;
    }

    public String getShortageItemSizeC() {
        return shortageItemSizeC;
    }

    public String getShortageItemVendorC() {
        return shortageItemVendorC;
    }

    public String getStatus() {
        return status;
    }

    public String getSubTypeC() {
        return subTypeC;
    }

    public String getType() {
        return type;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setSubTypeC(String subTypeC) {
        this.subTypeC = subTypeC;
    }
}