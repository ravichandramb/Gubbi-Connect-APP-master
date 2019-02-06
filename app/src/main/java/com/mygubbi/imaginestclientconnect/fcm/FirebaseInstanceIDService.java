/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mygubbi.imaginestclientconnect.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        Log.d(TAG, "onTokenRefresh: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        /*if (Utility.isNullOrEmpty(SharedPreferenceHelper.getToken(this))) {
            return;
        }

        VolleyService volleyService = new VolleyService(new IResult() {
            @Override
            public void notifySuccess(String url, String response) {
            }

            @Override
            public void notifyError(String url, VolleyError VolleyError, String error) {
            }

        }, this);
        Map<String, String> params = new HashMap<>();
        params.put("access_token", SharedPreferenceHelper.getToken(this));
        params.put("mihtra_id", SharedPreferenceHelper.getMihtraId(this));
        params.put("device_type", "android");
        params.put("device_id", token);

        Log.d(TAG, "sendRegistrationToServer: " + params.toString());

        if (!Utility.isNullOrEmpty(SharedPreferenceHelper.getToken(this))) {
            volleyService.postDataVolley(EndPoint.DEVICE_REGISTER, params);
        }*/
    }
}