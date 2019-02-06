package com.mygubbi.imaginestclientconnect.models;

import java.util.List;

public class ClientFeedbackQuestion {

    public static final String TYPE_OPTION = "0";
    public static final String TYPE_TEXT = "1";
    public static final String TYPE_RATING = "2";

    private String questionCode;
    private String questionType;
    private String question;
    private List<Option> options;
    private String questionGroupCode;

    private int rating;
    private String userRemarks;

    public ClientFeedbackQuestion(String questionCode, String questionType, String question, List<Option> options,String questionGroupCode) {
        this.questionCode = questionCode;
        this.questionType = questionType;
        this.question = question;
        this.options = options;
        this.questionGroupCode=questionGroupCode;
    }

    public String getQuestionCode() {
        return questionCode;
    }
    public String getquestionGroupCode() {
        return questionGroupCode;
    }
    public void setquestionGroupCode(String questionGroupCode) {
        this.questionGroupCode = questionGroupCode;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getQuestion() {
        return question;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserRemarks() {
        return userRemarks;
    }

    public void setUserRemarks(String userRemarks) {
        this.userRemarks = userRemarks;
    }

    @Override
    public String toString() {
        return "ClientFeedbackQuestion{" +
                "questionCode='" + questionCode + '\'' +
                ", questionType='" + questionType + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", rating=" + rating +
                ", userRemarks='" + userRemarks + '\'' +
                ", questionGroupCode='" + questionGroupCode + '\'' +
                '}';
    }
}