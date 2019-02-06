package com.mygubbi.imaginestclientconnect.clientIssues.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectHelper;
import com.mygubbi.imaginestclientconnect.helpers.GlideApp;
import com.mygubbi.imaginestclientconnect.models.ClientIssue;

/**
 * @author Hemanth
 * @since 4/23/2018
 */
public class ClientIssueDetailActivity extends AppCompatActivity {

    private ClientIssue issue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);

        Intent intent = getIntent();
        if (intent != null) {
            issue = intent.getParcelableExtra(ClientConnectConstants.CLIENT_ISSUE);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_issue_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setUpViews();
    }

    private void setUpViews() {
        TextView textViewIssue = findViewById(R.id.text_issue);
        ImageView imageView = findViewById(R.id.image_issue_detail);

        textViewIssue.setText(ClientConnectHelper.upperCaseFirst(issue.getName()));

        if (!TextUtils.isEmpty(issue.getDocumentUrl())) {
            GlideApp.with(this)
                    .load(issue.getDocumentUrl())
                    .centerCrop()
                    .into(imageView);
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
        super.onBackPressed();
        finish();
    }
}