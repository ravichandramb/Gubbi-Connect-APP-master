package com.mygubbi.imaginestclientconnect.clientIssues;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientBaseView;
import com.mygubbi.imaginestclientconnect.models.ClientIssue;

import java.util.List;

/**
 * @author Hemanth
 * @since 4/6/2018
 */
public interface ClientIssueView extends ClientBaseView {

    void showClientSupportDetails(List<ClientIssue> openIssuesList, List<ClientIssue> completedIssuesList);

    void onSupportCreated(String message);

    void onSupportCreationFailed();
}