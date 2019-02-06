package com.mygubbi.imaginestclientconnect.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mygubbi.imaginestclientconnect.R;


public class ForgotPasswordActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextInputLayout edtEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
       // toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  toolbar.setTitle("Forgot Password");
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        edtEmail = (TextInputLayout) findViewById(R.id.tl_email);
        btnResetPassword = (Button) findViewById(R.id.btn_reset);

        btnResetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getEditText().getText().toString().trim();
                if (Utility.isNullOrEmpty(email)) {
                    displayMessage(getResources().getString(R.string.email_error));
                } else if (!Utility.isValidEmail(email)) {
                    displayMessage(getResources().getString(R.string.invalid_email_error));
                    return;
                } else {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        displayMessage(getResources().getString(R.string.password_reset_link_msg));
                                        finish();
                                    } else {
                                        displayMessage(getResources().getString(R.string.fail_to_send_reset_email));
                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
