package com.mygubbi.imaginestclientconnect.clientProjects;

import android.util.Log;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.models.ClientProject;
import com.mygubbi.imaginestclientconnect.rest.RestClientConnectClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Hemanth
 * @since 4/12/2018
 */
public class ClientProjectsPresenter {

    private static final String TAG = "ClientUpdatesPresenter";

    private ClientProjectsView clientUpdatesView;

    ClientProjectsPresenter(ClientProjectsView clientUpdatesView) {
        this.clientUpdatesView = clientUpdatesView;
    }

    public void getClientProjectUpdates() {
        clientUpdatesView.showProgress("Loading project details...");

        RestClientConnectClient.getInstance().getApiService()
                .getClientProjectUpdates(ClientConnectApplication.getInstance().getOpportunityId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ClientProject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ClientProject> clientProjects) {
                        clientUpdatesView.hideProgress();
                        clientUpdatesView.showProjectUpdates((ArrayList<ClientProject>) clientProjects);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                        clientUpdatesView.hideProgress();
                        clientUpdatesView.showProjectUpdates(new ArrayList<>());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        clientUpdatesView.hideProgress();
                    }
                });
    }
}