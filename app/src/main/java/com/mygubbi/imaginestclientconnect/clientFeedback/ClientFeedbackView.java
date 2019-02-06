package com.mygubbi.imaginestclientconnect.clientFeedback;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientBaseView;
import com.mygubbi.imaginestclientconnect.models.FeedbackQuestionGroup;

import java.util.List;

public interface ClientFeedbackView extends ClientBaseView {

    void showFeedbackQuestion(List<FeedbackQuestionGroup> feedbackQuestionGroups);

    void showFeedbackSubmitted(boolean result);
}