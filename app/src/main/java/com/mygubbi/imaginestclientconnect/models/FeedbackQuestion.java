package com.mygubbi.imaginestclientconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Hemanth
 * @since 4/24/2018
 */
public class FeedbackQuestion {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("questionGroupCode")
    @Expose
    private String questionGroupCode;
    @SerializedName("questionCode")
    @Expose
    private String questionCode;
    @SerializedName("questionGroupTitle")
    @Expose
    private String questionGroupTitle;
    @SerializedName("questionTitle")
    @Expose
    private String questionTitle;
    @SerializedName("optionType")
    @Expose
    private String optionType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionGroupCode() {
        return questionGroupCode;
    }

    public void setQuestionGroupCode(String questionGroupCode) {
        this.questionGroupCode = questionGroupCode;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getQuestionGroupTitle() {
        return questionGroupTitle;
    }

    public void setQuestionGroupTitle(String questionGroupTitle) {
        this.questionGroupTitle = questionGroupTitle;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

}