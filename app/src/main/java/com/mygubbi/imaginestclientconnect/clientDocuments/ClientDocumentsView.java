package com.mygubbi.imaginestclientconnect.clientDocuments;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientBaseView;
import com.mygubbi.imaginestclientconnect.models.ClientDocument;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 3/29/2018
 */
public interface ClientDocumentsView extends ClientBaseView {

    void showDocuments(ArrayList<ClientDocument> documentsList);

}