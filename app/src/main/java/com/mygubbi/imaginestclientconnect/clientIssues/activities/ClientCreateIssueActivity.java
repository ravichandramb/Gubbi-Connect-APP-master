package com.mygubbi.imaginestclientconnect.clientIssues.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.clientIssues.ClientIssuePresenter;
import com.mygubbi.imaginestclientconnect.clientIssues.ClientIssueView;
import com.mygubbi.imaginestclientconnect.clientIssues.adapters.IssuesPhotoAdapter;
import com.mygubbi.imaginestclientconnect.helpers.RecyclerTouchListener;
import com.mygubbi.imaginestclientconnect.helpers.RecyclerViewClickListener;
import com.mygubbi.imaginestclientconnect.models.ClientIssue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * @author Hemanth
 * @since 3/28/2018
 */
public class ClientCreateIssueActivity extends AppCompatActivity implements ClientIssueView {

    private static final String TAG = "ClientCreateSupport";

    private EditText editIssue;
    private RecyclerView recyclerViewPhotos;
    private ProgressDialog progressDialog;
    private IssuesPhotoAdapter issuesPhotoAdapter;
    private ClientIssuePresenter clientSupportPresenter;

    private ArrayList<String> photosList;
    private ArrayList<File> filesList;

    private static final int REQUEST_CAMERA = 1234;
    private static final int REQUEST_STORAGE = 4535;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_create_issue);

        Toolbar toolbar = findViewById(R.id.toolbar_create_issue);
        toolbar.setTitle("Create Issue");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        photosList = new ArrayList<>();
        filesList = new ArrayList<>();
        clientSupportPresenter = new ClientIssuePresenter(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        setUpViews();
    }

    private void setUpViews() {
        editIssue = findViewById(R.id.edit_issue_description);

        Button buttonSaveIssue = findViewById(R.id.button_save_issue);

        recyclerViewPhotos = findViewById(R.id.recycler_view_photos);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        issuesPhotoAdapter = new IssuesPhotoAdapter(this, photosList);
        recyclerViewPhotos.setAdapter(issuesPhotoAdapter);

        buttonSaveIssue.setOnClickListener(v -> createIssue());

        recyclerViewPhotos.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerViewPhotos,
                new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (position == photosList.size()) {
                            showOptionsDialog();
                        } else {
                            Intent intent = new Intent(ClientCreateIssueActivity.this, FullImageViewActivity.class);
                            intent.putStringArrayListExtra("images", photosList);
                            intent.putExtra("position", position);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));
    }

    private void showOptionsDialog() {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.image_options_bottom_sheet, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        TextView txtFromCamera = view.findViewById(R.id.text_camera);
        TextView txtFromGallery = view.findViewById(R.id.text_gallery);
        TextView txtCancel = view.findViewById(R.id.text_cancel);

        txtFromCamera.setOnClickListener(v -> {
            dialog.dismiss();
            if (checkCameraPermission()) {
                EasyImage.openCamera(this, 0);
            }
        });

        txtFromGallery.setOnClickListener(v -> {
            dialog.dismiss();
            if (checkStoragePermission()) {
                EasyImage.openGallery(this, 0);
            }
        });

        txtCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private boolean validate(String issueText) {
        return !TextUtils.isEmpty(issueText);
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) ->
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_STORAGE));
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) ->
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA));
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                EasyImage.openCamera(this, 0);
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                EasyImage.openGallery(this, 0);
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                for (int i = 0; i < imageFiles.size(); i++) {
                    String imagePath = imageFiles.get(i).getAbsolutePath();

                    if (photosList.isEmpty() && filesList.isEmpty()) {
                        photosList.add(imagePath);
                        filesList.add(imageFiles.get(i));
                    } else {
                        photosList.set(0, imagePath);
                        filesList.set(0, imageFiles.get(i));
                    }
                }

                recyclerViewPhotos.setVisibility(View.VISIBLE);
                issuesPhotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                // Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(ClientCreateIssueActivity.this);
                    if (photoFile != null) {
                        //noinspection ResultOfMethodCallIgnored
                        photoFile.delete();
                    }
                }
            }
        });
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

    private void createIssue() {
        if (validate(editIssue.getText().toString().trim())) {
            clientSupportPresenter.createSupportTicket(ClientConnectApplication.getInstance().getOpportunityId(),
                    editIssue.getText().toString().trim(), filesList);
        } else {
            Toast.makeText(this, "Please provide issue description.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showClientSupportDetails(List<ClientIssue> openIssuesList, List<ClientIssue> closedIssuesList) {

    }

    @Override
    public void onSupportCreated(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onSupportCreationFailed() {
        Toast.makeText(this, "Failed to create new contacts", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
