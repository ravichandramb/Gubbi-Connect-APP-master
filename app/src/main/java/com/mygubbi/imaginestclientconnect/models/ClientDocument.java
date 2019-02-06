package com.mygubbi.imaginestclientconnect.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hemanth
 * @since 3/28/2018
 */
public class ClientDocument implements Parcelable {

    @SerializedName("OpportunityId")
    @Expose
    private String opportunityId;
    @SerializedName("docCategory")
    @Expose
    private String docCategory;
    @SerializedName("docType")
    @Expose
    private String docType;
    @SerializedName("docUrl")
    @Expose
    private String docUrl;
    @SerializedName("documentName")
    @Expose
    private String documentName;
    @SerializedName("file_ext")
    @Expose
    private String fileExt;

    private String downloadStatus;
    private int downloadProgress;
    private boolean fileDownloaded;

    private ClientDocument(Parcel in) {
        opportunityId = in.readString();
        docCategory = in.readString();
        docType = in.readString();
        docUrl = in.readString();
        documentName = in.readString();
        fileExt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(opportunityId);
        dest.writeString(docCategory);
        dest.writeString(docType);
        dest.writeString(docUrl);
        dest.writeString(documentName);
        dest.writeString(fileExt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientDocument> CREATOR = new Creator<ClientDocument>() {
        @Override
        public ClientDocument createFromParcel(Parcel in) {
            return new ClientDocument(in);
        }

        @Override
        public ClientDocument[] newArray(int size) {
            return new ClientDocument[size];
        }
    };

    public String getOpportunityId() {
        return opportunityId;
    }

    public String getDocCategory() {
        return docCategory;
    }

    public String getDocType() {
        return docType;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public boolean isFileDownloaded() {
        return fileDownloaded;
    }

    public void setFileDownloaded(boolean fileDownloaded) {
        this.fileDownloaded = fileDownloaded;
    }
}