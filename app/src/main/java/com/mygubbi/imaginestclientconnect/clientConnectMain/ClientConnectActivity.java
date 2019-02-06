package com.mygubbi.imaginestclientconnect.clientConnectMain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientAccount.ClientAccountActivity;
import com.mygubbi.imaginestclientconnect.clientAssistant.ClientAssistantActivity;
import com.mygubbi.imaginestclientconnect.clientConnectMain.fragments.ClientConnectFragment;
import com.mygubbi.imaginestclientconnect.clientConnectMain.fragments.NewClientFragment;
import com.mygubbi.imaginestclientconnect.clientFeedback.ClientFeedbackFragment;
import com.mygubbi.imaginestclientconnect.clientIssues.ClientIssueFragment;
import com.mygubbi.imaginestclientconnect.clientIssues.activities.ClientCreateIssueActivity;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.OnFragmentInteractionListener;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;

/**
 * @author Hemanth
 * @since 3/27/2018
 */
public class ClientConnectActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final String TAG = "ClientConnectActivity";

    private Toolbar toolbar;
    private FloatingActionButton fabCreateIssue;
    public static String feedbackType;
    private FragmentManager fragmentManager;
    public static String salesStage;

    private boolean isNewUser;
    ClientFeedbackFragment feedbackFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_connect);
        feedbackType="";
        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Client Connect");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        if (intent != null) {
            isNewUser = intent.getBooleanExtra(ClientConnectConstants.IS_NEW_USER, false);
        }

        setUpViews(isNewUser);


        feedbackFragment = new ClientFeedbackFragment();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(feedbackFragment)
                .commit();

    }

    private void setUpViews(boolean isNewUser) {
        fragmentManager = getSupportFragmentManager();
        if (isNewUser) {
            fragmentManager.beginTransaction().replace(R.id.container_layout, new NewClientFragment()).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.container_layout, new ClientConnectFragment()).commit();
        }

        LinearLayout layoutHome = findViewById(R.id.layout_home);
        LinearLayout layoutAssistant = findViewById(R.id.layout_assistant);
        LinearLayout layoutAccount = findViewById(R.id.layout_account);
        fabCreateIssue = findViewById(R.id.fab_create_issue);

        fabCreateIssue.bringToFront();

        layoutHome.setOnClickListener(v -> {
            if (isNewUser) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container_layout, new NewClientFragment(), Integer.toString(getFragmentCount()))
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.container_layout, new ClientConnectFragment(), Integer.toString(getFragmentCount()))
                        .commit();
            }
        });

        layoutAssistant.setOnClickListener(v -> navigateToClass(ClientAssistantActivity.class));

        layoutAccount.setOnClickListener(v -> navigateToClass(ClientAccountActivity.class));

        fabCreateIssue.setOnClickListener(v -> startActivity(new Intent(ClientConnectActivity.this, ClientCreateIssueActivity.class)));
    }

    private void navigateToClass(Class aClass) {
        fabCreateIssue.hide();
        Intent intent = new Intent(ClientConnectActivity.this, aClass);
        intent.putExtra("fromMain", true);
        startActivity(intent);
    }

    private int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    private SpannableString getSpannedString(String key, String value) {
        SpannableString spannableSalesString = new SpannableString(key + "\n" + value);
        spannableSalesString.setSpan(new RelativeSizeSpan(1.2f), spannableSalesString.length() - value.length(),
                spannableSalesString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        return spannableSalesString;
    }

    @Override
    public void onBackPressed() {
        fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.container_layout);

        fabCreateIssue.hide();
        fabCreateIssue.setVisibility(View.GONE);

        if (fragment instanceof ClientConnectFragment
                || fragment instanceof NewClientFragment) {
            super.onBackPressed();
        } else if (getFragmentCount() == 0) {
            fragmentManager.popBackStack();
            if (isNewUser) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container_layout, new NewClientFragment(), Integer.toString(getFragmentCount()))
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.container_layout, new ClientConnectFragment(), Integer.toString(getFragmentCount()))
                        .commit();
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        } else {
            super.onBackPressed();
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
    public void onFragmentInteraction(String title, Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_layout, fragment, Integer.toString(getFragmentCount()))
                .commit();

        toolbar.setTitle(title);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (fragment instanceof ClientIssueFragment) {
            fabCreateIssue.hide();
            fabCreateIssue.show();
            fabCreateIssue.bringToFront();
        } else {
            fabCreateIssue.hide();
        }
    }

    @Override
    public void onFragmentVisible() {
        fabCreateIssue.hide();
        fabCreateIssue.show();
        fabCreateIssue.bringToFront();
    }

    @Override
    public void onFragmentRemoved() {
        fabCreateIssue.hide();
    }

    @Override
    public void onProfileUpdated(ClientProfile clientProfile) {
        toolbar.setTitle("");
        TextView textStage = toolbar.findViewById(R.id.text_stage);
        TextView textCompletionDate = toolbar.findViewById(R.id.text_completion_date);

          salesStage = clientProfile.getSalesStage();
      //  salesStage = "Project Handover test";
        String completionDate = clientProfile.getProjectCompletionDate();
        System.out.println("completionDate null or not : "+completionDate);
        if(completionDate.equals("null")){
            textCompletionDate.setText(getSpannedString("Expected Completion Date",""));
            }
        else{
            textCompletionDate.setText(getSpannedString("Expected Completion Date", completionDate));
            }
        textStage.setText(getSpannedString("Stage", salesStage));
        String str = clientProfile.getFeedbackSubmitted();
        System.out.println("FeedbackSubmitted or not :: "+str);
       }

    @Override
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}