package com.mygubbi.imaginestclientconnect.clientDocuments;

import android.util.Log;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.models.ClientDocument;
import com.mygubbi.imaginestclientconnect.rest.RestClientConnectClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Hemanth
 * @since 3/29/2018
 */
public class ClientDocumentsPresenter {

    private static final String TAG = "DocumentsPresenter";

    private ClientDocumentsView documentsView;

    ClientDocumentsPresenter(ClientDocumentsView documentsView) {
        this.documentsView = documentsView;
    }

    public void getDocumentsList() {
        RestClientConnectClient.getInstance().getApiService()
                .getClientDocumentsList(ClientConnectApplication.getInstance().getOpportunityId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ClientDocument>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        documentsView.showProgress("Loading documents...");
                    }

                    @Override
                    public void onNext(List<ClientDocument> clientDocuments) {
                        Log.d(TAG, "onNext: ");
                        documentsView.showDocuments((ArrayList<ClientDocument>) clientDocuments);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                        documentsView.showDocuments(new ArrayList<>());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }
}