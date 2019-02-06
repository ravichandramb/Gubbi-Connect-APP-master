package com.mygubbi.imaginestclientconnect.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectActivity;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectHelper;
import com.mygubbi.imaginestclientconnect.models.User;

import java.util.HashMap;

import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

@SuppressLint("LogNotTimber")
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ProgressDialog progressDialog;
    private LoginButton buttonFBLogin;
    private TextView textEmailLogin;
    private LinearLayout layoutOverlay, layoutGoogleSignIn, layoutFBSignIn;
    private TextInputEditText editTextFirstName, editTextLastName, editTextPhone, editTextEmail, editTextPassword;
    private Button buttonSignUp;
    private BottomSheetBehavior bottomSheetBehavior;

    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    TextView forgotPassword;

    private static final int RC_SIGN_IN = 670;

    ProgressDialog nDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login/*activity_login_old*/);

        sharedPreferences = getSharedPreferences(ClientConnectConstants.CLIENT_CONNECT_PREFS, MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        forgotPassword=(TextView)findViewById(R.id.forgotpwd);


        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        if (sharedPreferences.getBoolean(ClientConnectConstants.USER_LOGGED_IN, false)) {
            boolean isNewUser = sharedPreferences.getBoolean(ClientConnectConstants.IS_NEW_USER, false);
            navigateToHome(isNewUser);
        } else {
            setUpViews();
        }

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callToForgotPasswordActivity();

                }
        });
    }

   public void  callToForgotPasswordActivity(){
       Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       getApplicationContext().startActivity ( intent );

    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == STATE_EXPANDED) {
            layoutOverlay.setVisibility(View.GONE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finishAffinity();
            super.onBackPressed();
        }
    }

    private void setUpViews() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        EditText editEmail = findViewById(R.id.edit_email);
        EditText editPassword = findViewById(R.id.edit_password);
        TextView textRegister = findViewById(R.id.text_register);
        textEmailLogin = findViewById(R.id.text_email_login);
        layoutGoogleSignIn = findViewById(R.id.layout_google);
        layoutFBSignIn = findViewById(R.id.layout_fb);

        String dontHaveAccount = getResources().getString(R.string.dont_have_account);
        String register = getResources().getString(R.string.register);

        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(dontHaveAccount);
        spannableString.append(" ");
        spannableString.append(register);

        int spanStart = dontHaveAccount.length() + 1;
        int spanEnd = spannableString.length();

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textRegister.setText(spannableString);

        layoutOverlay = findViewById(R.id.overlay);
        buttonFBLogin = findViewById(R.id.btn_fb_sign_in);

        textEmailLogin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(editEmail.getText().toString().trim())
                    && !TextUtils.isEmpty(editPassword.getText().toString().trim())) {
                authenticateUserFromFirebase(editEmail.getText().toString().trim(),
                        editPassword.getText().toString().trim());
                nDialog = new ProgressDialog(LoginActivity.this);
                nDialog.setMessage("Please Wait..");
                nDialog.setTitle("Loading..");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();

            } else {
                showToast("Please enter all fields");
            }
        });

        textEmailLogin.setOnLongClickListener(v -> {
//            navigateToHome(true);
            return false;
        });

        textRegister.setOnClickListener(v -> showRegisterSheet(false, "", null));

        layoutGoogleSignIn.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        layoutFBSignIn.setOnClickListener(v -> buttonFBLogin.performClick());

        CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();

        buttonFBLogin.setReadPermissions("email", "public_profile");
        buttonFBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        authenticateUserFromFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel: ");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        error.printStackTrace();
                        if (error.getMessage().contains("CONNECTION_FAILURE")) {
                            showToast("Please check your internet connection");
                        }
                    }
                }
        );

        View bottomSheet = findViewById(R.id.layout_register_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == STATE_EXPANDED) {
                    enableButtons(false);
                } else {
                    enableButtons(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @SuppressLint("LogNotTimber")
    private void authenticateUserFromFirebase(@NonNull String email, @NonNull String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        /*if (user.isEmailVerified()) {

                        } else {
                            showToast("Please verify email-id");
                        }*/
                        sharedPreferences.edit().putString(ClientConnectConstants.FIREBASE_USER_ID, user.getUid()).apply();
                        String displayName = user.getDisplayName();
                        sharedPreferences.edit().putString(ClientConnectConstants.FIREBASE_USER_NAME, displayName).apply();
                        if (user.getPhotoUrl() != null) {
                            String photoUrl = user.getPhotoUrl().toString();
                            sharedPreferences.edit().putString(ClientConnectConstants.FIREBASE_USER_PHOTO,
                                    photoUrl).apply();
                        }
                        getUserProfile(user.getUid());
                      //  nDialog.dismiss();

                    } else {
                        Log.e(TAG, "authenticateUserFromFirebase: ", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException
                                || task.getException() instanceof FirebaseAuthInvalidUserException) {
                            showToast("User doesn't exist. Please register");

                        } else {
                            showToast(ClientConnectConstants.ERROR_SOMETHING_WRONG);
                        }

                       nDialog.dismiss();
                    }
                });
    }

    private void authenticateUserFromGoogle(@NonNull GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authenticateWithSocial(credential);
    }

    private void authenticateUserFromFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        authenticateWithSocial(credential);
    }

    private void authenticateWithSocial(AuthCredential credential) {
        showProgress("Authenticating...");
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    hideProgress();
                    if (task.isSuccessful()) {
                        Log.d(TAG, "authenticateWithSocial: Success");
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {
                            onSocialSignInFinished(currentUser.getUid());
                        } else {
                            showToast("Failed to authenticate");
                        }
                    } else {
                        Log.e(TAG, "authenticateWithSocial: ", task.getException());
                        showToast("Failed to authenticate");
                    }
                });
    }

    @SuppressWarnings("SameParameterValue")
    private void showRegisterSheet(boolean isSocialLogin, String email, @Nullable String userId) {
        layoutOverlay.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(STATE_EXPANDED);

        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonSignUp = findViewById(R.id.btn_sign_up);

        editTextFirstName.addTextChangedListener(signUpTextWatcher);
        editTextLastName.addTextChangedListener(signUpTextWatcher);
        editTextEmail.addTextChangedListener(signUpTextWatcher);
        editTextPassword.addTextChangedListener(signUpTextWatcher);

        editTextEmail.setText(email);

//        editTextPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        buttonSignUp.setOnClickListener(v -> {
            if (ClientConnectHelper.isValidEmail(editTextEmail.getText().toString().trim())) {
                User user = new User();
                user.setName(editTextFirstName.getText().toString().trim());
                user.setEmail(editTextEmail.getText().toString().trim());
                user.setPhoneNo(editTextPhone.getText().toString().trim());
                if (isSocialLogin) {
                    user.setUid(userId);
                    updateUserProfile(null, user);
                } else {
                    signUpUser(user, editTextPassword.getText().toString().trim());
                }
            } else {
                Toast.makeText(this, "Please provide valid email-id", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpUser(User user, String password) {
        showProgress("Signing up...");
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    hideProgress();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        user.setUid(firebaseUser.getUid());

                        updateUserProfile(firebaseUser, user);
                    } else {
                        if (task.getException() != null) {
                            showToast(task.getException().getMessage());
                        }
                    }
                });
    }

    private void onSocialSignInFinished(final String userId) {
        showProgress("Verifying...");

        databaseReference.child("users")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            getUserProfile(userId);
                        } else {
                            hideProgress();
                            showToast("User doesn't exist. Please register");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        hideProgress();
                        showToast(ClientConnectConstants.ERROR_SOMETHING_WRONG);
                        databaseError.toException().printStackTrace();
                    }
                });
    }

    private void getUserProfile(String userId) {
        databaseReference.child("user-profiles")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            HashMap mapUserProfile = (HashMap) dataSnapshot.getValue();
                            if (mapUserProfile != null) {

                                String crmId = (String) mapUserProfile.get("crmId");

                                if (!TextUtils.isEmpty(crmId)) {
                                    ClientConnectApplication.getInstance().setOpportunityId(crmId);

                                    hideProgress();

                                    getToken(false);
                                } else {
                                    hideProgress();
                                    showToast("User profile is not update. Please contact MyGubbi support team");
                                    getToken(true);
                                }
                            } else {
                                hideProgress();
                                showToast("User profile is not update. Please contact MyGubbi support team");
                                getToken(true);
                            }
                        } else {
                            hideProgress();
                            showToast("User profile is not update. Please contact MyGubbi support team");
                            getToken(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        hideProgress();
                        showToast(ClientConnectConstants.ERROR_SOMETHING_WRONG);
                        databaseError.toException().printStackTrace();
                    }
                });
    }

    private void updateUserProfile(@Nullable FirebaseUser firebaseUser, User user) {
        showProgress("Creating profile...");
        databaseReference.child("users")
                .child(user.getUid())
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    hideProgress();
                    if (firebaseUser != null) {
                        if (bottomSheetBehavior.getState() == STATE_EXPANDED) {
                            layoutOverlay.setVisibility(View.GONE);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        Toast.makeText(getApplicationContext(), "Registration Successful. " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    hideProgress();
                    showToast("Failed to create user");
                });
    }

    @SuppressLint("LogNotTimber")
    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    if (bottomSheetBehavior.getState() == STATE_EXPANDED) {
                        layoutOverlay.setVisibility(View.GONE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registration Successful. Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "sendEmailVerification failed!", task.getException());
                        Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getToken(boolean isNewUser) {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUser.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.d(TAG, "see token:" + idToken);
                            sharedPreferences.edit().putString(ClientConnectConstants.FIREBASE_AUTH_TOKEN, idToken).apply();
                            loginSuccess(isNewUser);
                        } else {
                            showToast(ClientConnectConstants.ERROR_SOMETHING_WRONG);
                        }
                    });
        } else {
            showToast(ClientConnectConstants.ERROR_SOMETHING_WRONG);
        }
    }

    private void enableButtons(boolean isEnabled) {
        if (isEnabled) {
            layoutOverlay.setVisibility(View.GONE);
        } else {
            layoutOverlay.setVisibility(View.VISIBLE);
        }

        textEmailLogin.setEnabled(isEnabled);
        textEmailLogin.setClickable(isEnabled);
        layoutGoogleSignIn.setEnabled(isEnabled);
        layoutGoogleSignIn.setClickable(isEnabled);
        layoutFBSignIn.setEnabled(isEnabled);
        layoutFBSignIn.setClickable(isEnabled);
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void loginSuccess(boolean isNewUser) {
        showToast("Log in success!!");

        sharedPreferences.edit().putBoolean(ClientConnectConstants.USER_LOGGED_IN, true).apply();
        sharedPreferences.edit().putBoolean(ClientConnectConstants.IS_NEW_USER, isNewUser).apply();

        navigateToHome(isNewUser);
        nDialog.dismiss();
    }

    private void navigateToHome(boolean isNewUser) {
        Intent intent = new Intent(LoginActivity.this, ClientConnectActivity.class);
        intent.putExtra(ClientConnectConstants.IS_NEW_USER, isNewUser);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Signed in successfully
                if (account != null && account.getEmail() != null) {
                    authenticateUserFromGoogle(account);
                }
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "signInResult: Failed code = " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private TextWatcher signUpTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateSignUpFields();
        }
    };

    private void validateSignUpFields() {
        if (!TextUtils.isEmpty(editTextFirstName.getText().toString().trim())
                && !TextUtils.isEmpty(editTextLastName.getText().toString().trim())
                && !TextUtils.isEmpty(editTextPhone.getText().toString().trim())
                && !TextUtils.isEmpty(editTextEmail.getText().toString().trim())
                && !TextUtils.isEmpty(editTextPassword.getText().toString().trim())) {
            buttonSignUp.setEnabled(true);
        } else {
            buttonSignUp.setEnabled(false);
        }
    }
}