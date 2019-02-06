package com.mygubbi.imaginestclientconnect.clientAssistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientAccount.ClientAccountActivity;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectActivity;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.RecyclerViewScrollListener;
import com.mygubbi.imaginestclientconnect.models.AccessToken;
import com.mygubbi.imaginestclientconnect.models.ChatBotMessage;
import com.mygubbi.imaginestclientconnect.models.ClientAssist;
import com.mygubbi.imaginestclientconnect.models.NotificationData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hemanth
 * @since 4/30/2018
 */
public class ClientAssistantActivity extends AppCompatActivity implements ClientAssistantView {

//    private static final String TAG = "ClientAssistantFragment";

    private RecyclerView recyclerViewAssistant;
    private ProgressBar progressBar;
    private EditText editQuery;

    private AssistantAdapter assistantAdapter;

    private ClientAssistantPresenter presenter;
    private ProgressDialog progressDialog;

    private ArrayList<ClientAssist> clientAssists;

    private int pageNumber = 0;
    private boolean isFirstCall = true;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        Toolbar toolbar = findViewById(R.id.toolbar_assistant);
        toolbar.setTitle("Assistant");
        setSupportActionBar(toolbar);

        SharedPreferences preferences = this.getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS, MODE_PRIVATE);
        userId = preferences.getString(ClientConnectConstants.FIREBASE_USER_ID, "");

        presenter = new ClientAssistantPresenter(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        clientAssists = new ArrayList<>();

        setUpViews();
    }

    public void setUpViews() {
        LinearLayout layoutHome = findViewById(R.id.layout_home);
        LinearLayout layoutAccount = findViewById(R.id.layout_account);
        FloatingActionButton fabSend = findViewById(R.id.fab_send);

        recyclerViewAssistant = findViewById(R.id.recycler_view_assistant);
        progressBar = findViewById(R.id.progress_bar_loader);
        editQuery = findViewById(R.id.edit_query);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerViewAssistant.setLayoutManager(layoutManager);

        assistantAdapter = new AssistantAdapter(this, clientAssists);
        recyclerViewAssistant.setAdapter(assistantAdapter);

        AccessToken accessToken = ClientConnectApplication.getInstance().getAccessToken();
        if (accessToken == null) {
            presenter.getOAuthToken(ClientConnectApplication.getInstance().getFirebaseToken());
        } else {
            presenter.getUserNotifications(userId, pageNumber);
        }

        fabSend.setOnClickListener(v -> {
            if (validate()) {
                String query = getInputQuery();
                clientAssists.add(0, new ClientAssist(new ChatBotMessage(query, true)));

                assistantAdapter.notifyItemInserted(clientAssists.size());
                assistantAdapter.notifyItemRangeInserted(clientAssists.size() - 1, 1);
                recyclerViewAssistant.smoothScrollToPosition(0);
                presenter.queryBot(query);

                editQuery.setText("");
            }
        });

        recyclerViewAssistant.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                ++pageNumber;
                if (!isFirstCall) {
                    presenter.getUserNotifications(userId, pageNumber);
                }
            }
        });

        layoutHome.setOnClickListener(v -> {
            Intent mainIntent = new Intent(ClientAssistantActivity.this, ClientConnectActivity.class);
            startActivity(mainIntent);
            finishAffinity();
        });

        layoutAccount.setOnClickListener(v -> {
            startActivity(new Intent(ClientAssistantActivity.this, ClientAccountActivity.class));
            finish();
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(getInputQuery())) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @NonNull
    private String getInputQuery() {
        return editQuery.getText().toString().trim();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void showProgress(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
        if (!isFirstCall) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void loginResult(boolean isSuccess) {
        if (isSuccess) {
            AccessToken accessToken = ClientConnectApplication.getInstance().getAccessToken();
            presenter.getUserNotifications(accessToken.getUserId(), pageNumber);
        }
    }

    @Override
    public void showNotifications(List<NotificationData> notificationData) {
        for (NotificationData notificationDatum : notificationData) {
            clientAssists.add(new ClientAssist(notificationDatum));
        }

        assistantAdapter.notifyItemRangeInserted(clientAssists.size() - notificationData.size(),
                notificationData.size());

        if (isFirstCall) {
            isFirstCall = false;
            recyclerViewAssistant.smoothScrollToPosition(0);
        }
    }

    @Override
    public void showBotResponse(ChatBotMessage chatBotMessage) {
        clientAssists.add(0, new ClientAssist(chatBotMessage));

        assistantAdapter.notifyItemInserted(clientAssists.size());
        assistantAdapter.notifyItemRangeInserted(clientAssists.size() - 1, 1);
        recyclerViewAssistant.smoothScrollToPosition(0);
    }

}