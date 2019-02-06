package com.mygubbi.imaginestclientconnect.clientHandover;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.AssetsReader;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClientSignatureActivity extends AppCompatActivity {

//    private static final String TAG = "ClientSignatureActivity";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signature);

        Toolbar toolbar = findViewById(R.id.toolbar_signature);
        toolbar.setTitle("Signature");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        TextView textSignHereMessage = findViewById(R.id.text_sign_here_message);
        TextView textTermsAndConditions = findViewById(R.id.text_terms_and_conditions);
        Button btnClear = findViewById(R.id.btn_clear);
        Button btnContinue = findViewById(R.id.btn_continue);
        SignaturePad signaturePad = findViewById(R.id.signature_pad);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                textSignHereMessage.setVisibility(View.GONE);
            }

            @Override
            public void onSigned() {
                btnClear.setEnabled(true);
                btnContinue.setEnabled(true);
            }

            @Override
            public void onClear() {
                btnClear.setEnabled(false);
                btnContinue.setEnabled(false);
                textSignHereMessage.setVisibility(View.VISIBLE);
            }
        });

        btnClear.setOnClickListener(v -> signaturePad.clear());

        textTermsAndConditions.setOnClickListener(v -> showAlert(null));

        btnContinue.setOnClickListener(v -> showAlert(signaturePad));
    }

    private void showAlert(@Nullable SignaturePad signaturePad) {
        AssetsReader assetsReader = new AssetsReader(this);

        if ((assetsReader.getTxtFile("txtfiles/terms_and_conditions.txt")) != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.terms_and_conditions))
                    .setMessage(assetsReader.getTxtFile("txtfiles/terms_and_conditions.txt"))
                    .setCancelable(false)
                    .setPositiveButton("I Agree", (dialog, which) -> {
                        dialog.dismiss();
                        if (signaturePad != null) {
                            showProgress("Saving signature...");
                            Bitmap signatureBitmap = signaturePad.getTransparentSignatureBitmap();
                            String imagePath = saveToInternalStorage(signatureBitmap);
                            hideProgress();

                            Intent intent = new Intent();
                            intent.putExtra(ClientConnectConstants.CLIENT_SIGNATURE, imagePath);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
                    .setNegativeButton("I Disagree", (dialog, which) -> dialog.dismiss());

            Dialog dialog = builder.create();
            dialog.show();
        }
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
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

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("clientImages", Context.MODE_PRIVATE);
        // Create clientImages directory
        File imagePath = new File(directory, "signatureImage.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imagePath.getAbsolutePath();
    }
}