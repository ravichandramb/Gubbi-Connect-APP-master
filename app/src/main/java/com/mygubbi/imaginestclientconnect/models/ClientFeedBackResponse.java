package com.mygubbi.imaginestclientconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientFeedBackResponse {

    @SerializedName("opportunityId")
    @Expose
    private String opportunityId;
    @SerializedName("questionCode")
    @Expose
    private String questionCode;
    @SerializedName("answerCode")
    @Expose
    private String answerCode;

    @SerializedName("questionGroupCode")
    @Expose
    private String questionGroupCode;

    public ClientFeedBackResponse(String opportunityId, String questionCode, String answerCode,String questionGroupCode) {
        this.opportunityId = opportunityId;
        this.questionCode = questionCode;
        this.answerCode = answerCode;
        this.questionGroupCode=questionGroupCode;
    }

    @Override
    public String toString() {
        return "ClientFeedBackResponse{" +
                "opportunityId='" + opportunityId + '\'' +
                ", questionCode='" + questionCode + '\'' +
                ", answerCode='" + answerCode + '\'' +
                "questionGroupCode='" + questionGroupCode + '\'' +
                '}';
    }
}