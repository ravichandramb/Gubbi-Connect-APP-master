package com.mygubbi.imaginestclientconnect.clientIssues.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientIssues.adapters.TouchImageAdapter;
import com.mygubbi.imaginestclientconnect.helpers.ExtendedViewPager;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 4/5/2018
 */
public class FullImageViewActivity extends AppCompatActivity implements TouchImageAdapter.ImageLoadListener {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);

        Toolbar toolbar = findViewById(R.id.toolbar_full_image);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progress_bar_loader);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setInterpolator(new OvershootInterpolator());

        ArrayList<String> imagesList = getIntent().getStringArrayListExtra("images");
        int position = getIntent().getIntExtra("position", 0);

        ExtendedViewPager viewPager = findViewById(R.id.view_pager_images);
        viewPager.setAdapter(new TouchImageAdapter(this, imagesList, this));
        viewPager.setCurrentItem(position);

        if (imagesList.size() > 1) {
            TabLayout tabLayout = findViewById(R.id.tab_indicator_images);
            tabLayout.setupWithViewPager(viewPager, true);
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
    public void onImageLoaded() {
        progressBar.setVisibility(View.GONE);
    }
}