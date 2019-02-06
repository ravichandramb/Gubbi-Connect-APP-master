package com.mygubbi.imaginestclientconnect.clientHandover;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientBaseView;
import com.mygubbi.imaginestclientconnect.models.ClientHandover;

import java.util.List;

public interface ClientHandoverView extends ClientBaseView {

    void showHandoverDetails(List<ClientHandover> clientHandoverList);

    void onHandoverFinished(boolean status, String message, String description);
}
