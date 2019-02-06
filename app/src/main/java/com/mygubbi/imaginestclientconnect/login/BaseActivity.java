package com.mygubbi.imaginestclientconnect.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.mygubbi.imaginestclientconnect.R;

public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

    }


    protected void displayMessage(String toastString) {
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()
                .isConnectedOrConnecting());
    }

    public void displayNetworkSetting(String msg, View view) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).setAction(getString(R.string.network_settings),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }

                }).show();
        return;

    }

    protected void gotoClass(Class<?> classTo) {
        startActivity(new Intent(this, classTo));
    }

    protected void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    protected void cancelDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }








}
