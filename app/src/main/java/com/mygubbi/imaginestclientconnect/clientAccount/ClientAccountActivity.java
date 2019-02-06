package com.mygubbi.imaginestclientconnect.clientAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientAssistant.ClientAssistantActivity;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectActivity;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.login.LoginActivity;

public class ClientAccountActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private boolean fromMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.toolbar_account);
        toolbar.setTitle("Account");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if (intent != null) {
            fromMain = intent.getBooleanExtra("fromMain", true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        LinearLayout layoutHome = findViewById(R.id.layout_home);
        LinearLayout layoutAssistant = findViewById(R.id.layout_assistant);

        layoutHome.setOnClickListener(v -> {
            Intent mainIntent = new Intent(ClientAccountActivity.this, ClientConnectActivity.class);
            startActivity(mainIntent);
            finishAffinity();
        });

        layoutAssistant.setOnClickListener(v -> {
            startActivity(new Intent(ClientAccountActivity.this, ClientAssistantActivity.class));
            finish();
        });

        CardView cardLogout = findViewById(R.id.card_logout);

        cardLogout.setOnClickListener(v -> logout());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS, MODE_PRIVATE);

        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();

        sharedPreferences.edit().clear().apply();
        ClientConnectApplication.getInstance().setOpportunityId(null);

        showProgress("Logging out...");

        new Handler().postDelayed(() -> {
            hideProgress();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClientAccountActivity.this, LoginActivity.class));
            finish();
        }, 1500);
    }

    public void showProgress(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}