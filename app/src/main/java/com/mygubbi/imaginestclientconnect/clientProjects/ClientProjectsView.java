package com.mygubbi.imaginestclientconnect.clientProjects;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientBaseView;
import com.mygubbi.imaginestclientconnect.models.ClientProject;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 4/12/2018
 */
public interface ClientProjectsView extends ClientBaseView {

    void showProjectUpdates(ArrayList<ClientProject> updatesList);
}