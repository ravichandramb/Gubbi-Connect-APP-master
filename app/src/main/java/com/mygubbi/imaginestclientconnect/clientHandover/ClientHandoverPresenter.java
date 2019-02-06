package com.mygubbi.imaginestclientconnect.clientHandover;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.models.APIResponse;
import com.mygubbi.imaginestclientconnect.models.ClientHandover;
import com.mygubbi.imaginestclientconnect.models.ClientHandoverResponse;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;
import com.mygubbi.imaginestclientconnect.rest.RestClientConnectClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ClientHandoverPresenter {

    private static final String TAG = "ClientHandoverPresenter";

    private ClientHandoverView clientHandoverView;

    ClientHandoverPresenter(ClientHandoverView clientHandoverView) {
        this.clientHandoverView = clientHandoverView;
    }

    public static String getProfilePhoneNumber(Context context) {
        String phoneNumber = "";
        SharedPreferences sharedPreferences = context.getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS, Context.MODE_PRIVATE);
        String profileData = sharedPreferences.getString(ClientConnectConstants.CLIENT_CONNECT_PROFILE, "");

        if (!TextUtils.isEmpty(profileData)) {
            Gson gson = new Gson();

            ClientProfile clientProfile = gson.fromJson(profileData, ClientProfile.class);

            if (clientProfile != null) {
                phoneNumber = clientProfile.getCustomerPhone();
            }

            if (phoneNumber.trim().length() == 10) {
                phoneNumber = "+91 " + phoneNumber;
            }
        }
        return phoneNumber;
//        return "+91 8660711418";
    }

    public void getHandoverDetails() {
        RestClientConnectClient.getInstance().getApiService()
                .getHandoverDetails(ClientConnectApplication.getInstance().getOpportunityId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ClientHandoverResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        clientHandoverView.showProgress("Loading handover details...");
                    }

                    @Override
                    public void onNext(List<ClientHandoverResponse> clientHandoverList) {
                        Log.d(TAG, "onNext: ");

                        HashMap<String, List<ClientHandover>> tempMap = new HashMap<>();
                        List<ClientHandover> clientHandovers = new ArrayList<>();

                        for (ClientHandoverResponse clientHandoverResponse : clientHandoverList) {
                            if (tempMap.containsKey(clientHandoverResponse.getSubTypeC())) {
                                List<ClientHandover> tempList = tempMap.get(clientHandoverResponse.getSubTypeC());
                                tempList.add(new ClientHandover(clientHandoverResponse.getRoomC(), false));
                                tempMap.put(clientHandoverResponse.getSubTypeC(), tempList);
                            } else {
                                List<ClientHandover> tempList = new ArrayList<>();
                                tempList.add(new ClientHandover(clientHandoverResponse.getRoomC(), false));
                                tempMap.put(clientHandoverResponse.getSubTypeC(), tempList);
                            }
                        }

                        for (String key : tempMap.keySet()) {
                            List<ClientHandover> tempList = tempMap.get(key);
                            tempList.add(0, new ClientHandover(key, true));
                            clientHandovers.addAll(tempList);
                        }

                        clientHandoverView.hideProgress();
                        clientHandoverView.showHandoverDetails(clientHandovers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                        clientHandoverView.hideProgress();
                        clientHandoverView.showHandoverDetails(new ArrayList<>());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        clientHandoverView.hideProgress();
                    }
                });
    }

    public void uploadHandoverFile() {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("opportunity_id", ClientConnectApplication.getInstance().getOpportunityId());

        File filePath = new File(Environment.getExternalStorageDirectory(), File.separator + "GubbiConnect" + File.separator + "Reports");
        File handoverFile = new File(filePath, "Completion_Report.pdf");

        builder.addFormDataPart("document", handoverFile.getName(), prepareFilePart(handoverFile));

        MultipartBody requestBody = builder.build();

        RestClientConnectClient.getInstance().getApiService().uploadHandoverFile(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<APIResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        clientHandoverView.showProgress("Saving file...");
                    }

                    @Override
                    public void onNext(APIResponse apiResponse) {
                        if (apiResponse != null && apiResponse.getStatus().equalsIgnoreCase("success")) {
                            clientHandoverView.onHandoverFinished(true, "Success", "PDF created successfully");
                        } else {
                            clientHandoverView.onHandoverFinished(false, "Failed", "Couldn't create PDF. Please try again");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        clientHandoverView.hideProgress();
                        clientHandoverView.onHandoverFinished(false, "Failed", "Couldn't create PDF. Please try again");
                    }

                    @Override
                    public void onComplete() {
                        clientHandoverView.hideProgress();
                    }
                });
    }

    @NonNull
    private RequestBody prepareFilePart(File file) {
        // create RequestBody instance from file
        return RequestBody.create(MediaType.parse("pdf/*"), file);
    }

}