package com.mygubbi.imaginestclientconnect.clientIssues;

import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.models.APIResponse;
import com.mygubbi.imaginestclientconnect.models.ClientIssue;
import com.mygubbi.imaginestclientconnect.rest.RestClientConnectClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author Hemanth
 * @since 4/6/2018
 */
public class ClientIssuePresenter {

    private static final String TAG = "ClientSupportPresenter";

    private ClientIssueView clientSupportView;

    public ClientIssuePresenter(ClientIssueView clientSupportView) {
        this.clientSupportView = clientSupportView;
    }

    public void getClientSupportList() {
        RestClientConnectClient.getInstance().getApiService()
                .getClientSupportList(ClientConnectApplication.getInstance().getOpportunityId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ClientIssue>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        clientSupportView.showProgress("Loading support tickets...");
                    }

                    @Override
                    public void onNext(List<ClientIssue> clientSupportList) {
                        List<ClientIssue> openIssuesList = new ArrayList<>();
                        List<ClientIssue> completedIssuesList = new ArrayList<>();

                        for (ClientIssue clientIssue : clientSupportList) {
                            if (clientIssue.getStatus().equalsIgnoreCase("Completed")) {
                                completedIssuesList.add(clientIssue);
                            } else {
                                openIssuesList.add(clientIssue);
                            }
                        }

                        clientSupportView.showClientSupportDetails(openIssuesList, completedIssuesList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        clientSupportView.hideProgress();
                        clientSupportView.showClientSupportDetails(new ArrayList<>(), new ArrayList<>());
                    }

                    @Override
                    public void onComplete() {
                        clientSupportView.hideProgress();
                    }
                });
    }

    public void createSupportTicket(String opportunityId, String issue, List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("opportunity_id", opportunityId);
        builder.addFormDataPart("issue", issue);

        for (File file : files) {
            builder.addFormDataPart("document", file.getName(), prepareFilePart(file));
        }

        MultipartBody requestBody = builder.build();

        RestClientConnectClient.getInstance().getApiService().createClientSupport(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<APIResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        clientSupportView.showProgress("Creating...");
                    }

                    @Override
                    public void onNext(APIResponse apiResponse) {
                        /*if (!apiResponse.getStatus().equals("Error")) {
                            clientSupportView.onSupportCreated(apiResponse.getMessage());
                        } else {
                            clientSupportView.onSupportCreationFailed();
                        }*/
                        clientSupportView.onSupportCreated("Success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        clientSupportView.hideProgress();
                        clientSupportView.onSupportCreationFailed();
                    }

                    @Override
                    public void onComplete() {
                        clientSupportView.hideProgress();
                    }
                });
    }

    @NonNull
    private RequestBody prepareFilePart(File file) {
        // create RequestBody instance from file
        return RequestBody.create(MediaType.parse("image/*"), file);
    }
}