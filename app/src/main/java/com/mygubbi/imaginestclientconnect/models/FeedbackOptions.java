package com.mygubbi.imaginestclientconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackOptions {

    @SerializedName("question_code")
    @Expose
    private String questionCode;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "FeedbackQuestionOptions{" +
                "questionCode='" + questionCode + '\'' +
                ", options=" + options +
                '}';
    }
}