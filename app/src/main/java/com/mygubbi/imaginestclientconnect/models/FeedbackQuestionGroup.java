package com.mygubbi.imaginestclientconnect.models;

public class FeedbackQuestionGroup {

    private String questionHeader;
    private ClientFeedbackQuestion feedbackQuestion;
    private int viewType;

    public FeedbackQuestionGroup(String questionHeader, int viewType) {
        this.questionHeader = questionHeader;
        this.viewType = viewType;
    }

    public FeedbackQuestionGroup(ClientFeedbackQuestion feedbackQuestion, int viewType) {
        this.feedbackQuestion = feedbackQuestion;
        this.viewType = viewType;
    }

    public String getQuestionHeader() {
        return questionHeader;
    }

    public void setQuestionHeader(String questionHeader) {
        this.questionHeader = questionHeader;
    }

    public ClientFeedbackQuestion getFeedbackQuestion() {
        return feedbackQuestion;
    }

    public void setFeedbackQuestion(ClientFeedbackQuestion feedbackQuestion) {
        this.feedbackQuestion = feedbackQuestion;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public String toString() {
        return "FeedbackQuestionGroup{" +
                "questionHeader='" + questionHeader + '\'' +
                ", feedbackQuestion=" + feedbackQuestion +
                ", viewType=" + viewType +
                '}';
    }
}