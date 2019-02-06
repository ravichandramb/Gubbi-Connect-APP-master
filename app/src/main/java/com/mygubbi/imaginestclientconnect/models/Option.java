package com.mygubbi.imaginestclientconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Option {

    @SerializedName("answer_code")
    @Expose
    private String answerCode;
    @SerializedName("answer_icon")
    @Expose
    private String answerIcon;

    private boolean userAnswer;

    public String getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
    }

    public String getAnswerIcon() {
        return answerIcon;
    }

    public void setAnswerIcon(String answerIcon) {
        this.answerIcon = answerIcon;
    }

    public boolean isUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(boolean userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public String toString() {
        return "Option{" +
                "answerCode='" + answerCode + '\'' +
                ", answerIcon='" + answerIcon + '\'' +
                '}';
    }
}