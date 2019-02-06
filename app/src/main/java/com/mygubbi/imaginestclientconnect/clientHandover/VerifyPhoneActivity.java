package com.mygubbi.imaginestclientconnect.clientHandover;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.OTPListener;
import com.mygubbi.imaginestclientconnect.helpers.OTPView;

import java.util.concurrent.TimeUnit;

@SuppressLint("LogNotTimber")
public class VerifyPhoneActivity extends AppCompatActivity implements OTPListener {

    private static final String TAG = "VerifyOTPActivity";

    private TextView textHeader, textTimer, textResendOTP;
    private OTPView otpView;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer;

    private String registeredPhoneNumber;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        Toolbar toolbar = findViewById(R.id.toolbar_verify_otp);
        toolbar.setTitle("Verify Phone Number");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        registeredPhoneNumber = ClientHandoverPresenter.getProfilePhoneNumber(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        textHeader = findViewById(R.id.text_header);
        textTimer = findViewById(R.id.text_timer);
        textResendOTP = findViewById(R.id.text_resend_otp);
        otpView = findViewById(R.id.otp_view);

        String text = getResources().getString(R.string.verify_your_number) + "\n" + registeredPhoneNumber;
        textHeader.setText(text);

        otpView.setListener(this);

        textResendOTP.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(registeredPhoneNumber)) {
                showProgress("Requesting OTP...");
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        registeredPhoneNumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        this,               // Activity (for callback binding)
                        mCallbacks,         // OnVerificationStateChangedCallbacks
                        mResendToken); // ForceResendingToken from callbacks
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideProgress();
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(VerifyPhoneActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.d(TAG, "onVerificationFailed: FirebaseTooManyRequestsException");
                } else if (e instanceof FirebaseAuthException) {
                    FirebaseAuthException authException = (FirebaseAuthException) e;
                    Log.d(TAG, "onVerificationFailed: " + authException.getErrorCode());
                }
                e.printStackTrace();
                Toast.makeText(VerifyPhoneActivity.this, ClientConnectConstants.ERROR_SOMETHING_WRONG, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                onOTPRequested(verificationId, token);
            }
        };

        requestOtp();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
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

    private void requestOtp() {
        showProgress("Requesting OTP...");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                registeredPhoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,
                VerifyPhoneActivity.this,
                mCallbacks);
    }

    public void onOTPRequested(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
        hideProgress();

        Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

        mVerificationId = verificationId;
        mResendToken = token;

        textResendOTP.setVisibility(View.GONE);
        textHeader.setVisibility(View.VISIBLE);
        textTimer.setVisibility(View.VISIBLE);
        otpView.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                textTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                textTimer.setVisibility(View.GONE);
                textResendOTP.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    hideProgress();
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        onOTPVerified();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            onOTPVerifyFailed();
                        }
                    }
                });
    }

    public void onOTPVerified() {
        hideProgress();
        Toast.makeText(this, "Mobile number verified", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(VerifyPhoneActivity.this, ClientSignatureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }

    private void onOTPVerifyFailed() {
        Toast.makeText(VerifyPhoneActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();

        if (countDownTimer != null) {
            countDownTimer.cancel();
            textTimer.setVisibility(View.GONE);
            textResendOTP.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onOTPEntered(String otp) {
        Log.d(TAG, "onCreate: " + otp);
        /*PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        showProgress("Verifying OTP...");
        signInWithPhoneAuthCredential(credential);*/
        onOTPVerified();
    }

    @Override
    public void onOTPPasted() {
        ClipboardManager clipboard = (ClipboardManager) VerifyPhoneActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData primaryClip = clipboard.getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() > 0) {
                CharSequence charSequence = primaryClip.getItemAt(0).coerceToText(this);
                if (charSequence.length() == 6 && charSequence.toString().matches("[0-9]+")) {
                    otpView.setOTP(charSequence.toString());
                } else {
                    Log.d(TAG, "onOTPPasted: Invalid clipboard data");
                }
            }
        }
    }
}