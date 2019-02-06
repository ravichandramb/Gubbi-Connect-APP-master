package com.mygubbi.imaginestclientconnect.clientHandover;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.OnFragmentInteractionListener;
import com.mygubbi.imaginestclientconnect.models.ClientHandover;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * @author Hemanth
 * @since 4/25/2018
 */
@SuppressLint("LogNotTimber")
public class ClientHandoverFragment extends Fragment implements ClientHandoverView,
        CreatePDFTask.CreatePDFListener {

    private static final String TAG = "ClientHandoverFragment";

    private View rootView;
    private LinearLayout layoutHandoverMain, layoutHandovers;
    private TextInputEditText editRemarks;

    private ArrayList<ClientHandover> clientHandovers;
    private String signatureImagePath;

    private static final int REQUEST_SIGNATURE = 287;
    private static final int REQUEST_STORAGE = 489;

    private ProgressDialog progressDialog;

    private ClientHandoverPresenter handoverPresenter;

    private OnFragmentInteractionListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handoverPresenter = new ClientHandoverPresenter(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_handover, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textNameAddress = rootView.findViewById(R.id.text_name_address);

        Button buttonSign = rootView.findViewById(R.id.btn_sign);

        layoutHandoverMain = rootView.findViewById(R.id.layout_handover_main);
        layoutHandovers = rootView.findViewById(R.id.layout_handovers);
        editRemarks = rootView.findViewById(R.id.edit_remarks);

        ClientProfile profile = ClientConnectApplication.getInstance().getClientProfile();

        String text = "Name and Address: ";

        if (profile != null) {
            text += profile.getCustomerName().trim();
        }

        textNameAddress.setText(text);

        buttonSign.setOnClickListener(v -> checkStoragePermission());

        handoverPresenter.getHandoverDetails();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startOTPVerification();
        } else {
            Toast.makeText(getContext(), "Storage permission denied", Toast.LENGTH_SHORT).show();
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNATURE && resultCode == RESULT_OK) {
            if (data != null) {
                signatureImagePath = data.getStringExtra(ClientConnectConstants.CLIENT_SIGNATURE);
                if (signatureImagePath != null) {
                    createPDF();
                }
            }
        }
    }

    @Override
    public void onCreatePDFStarted(String message) {
        showProgress(message);
    }

    @Override
    public void onPDFCreated(boolean isSuccess) {
        hideProgress();
        if (isSuccess) {
            handoverPresenter.uploadHandoverFile();
        } else {
            Toast.makeText(getContext(), "Failed to create PDF, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showHandoverDetails(List<ClientHandover> clientHandoverList) {
        this.clientHandovers = (ArrayList<ClientHandover>) clientHandoverList;

        for (int i = 0; i < clientHandovers.size(); i++) {
            if (getActivity() != null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.item_handover, null);

                ClientHandover handover = clientHandovers.get(i);

                TextView textSectionHeader = view.findViewById(R.id.text_section_header);
                TextView textItem = view.findViewById(R.id.text_item);

                View divider = view.findViewById(R.id.divider);

                LinearLayout layoutItems = view.findViewById(R.id.layout_items);

                if (handover.isHeader()) {
                    textSectionHeader.setVisibility(View.VISIBLE);
                    layoutItems.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);

                    textSectionHeader.setText(handover.getItem());
                } else {
                    textSectionHeader.setVisibility(View.GONE);
                    layoutItems.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.VISIBLE);

                    textItem.setText(handover.getItem());
                }

                view.setTag(i);

                layoutHandovers.addView(view);
            }
        }

        hideProgress();

        layoutHandoverMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHandoverFinished(boolean status, String message, String description) {
        if (status && getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle(message)
                    .setMessage(description)
                    .setPositiveButton("Show", (dialog, which) -> {
                        dialog.dismiss();
                        showPDF();
                    })
                    .setNegativeButton("Close", (dialog, which) -> {
                        dialog.dismiss();
                        listener.onBackClick();
                    });

            Dialog dialog = builder.create();
            dialog.show();

            clearCache();
        } else {
            Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext() != null && getActivity() != null) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startOTPVerification();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // We've been denied once before. Explain why we need the permission, then ask again.
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Storage Permission");
                    alertBuilder.setMessage("External storage permission is necessary to download files");
                    alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) ->
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_STORAGE));
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    // We've never asked. Just do it.
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE);
                }
            }
        }
    }

    private void startOTPVerification() {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            String profilePhoneNumber = ClientHandoverPresenter.getProfilePhoneNumber(getContext());
            String message = "Verify your registered mobile number\n" + profilePhoneNumber + " to continue";

            builder.setTitle("Verify OTP")
                    .setMessage(message)
                    .setPositiveButton("Send OTP", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(getContext(), VerifyPhoneActivity.class), REQUEST_SIGNATURE);
                    })
                    .setNegativeButton("Close", (dialog, which) -> dialog.dismiss());

            Dialog dialog = builder.create();
            dialog.show();
        }
    }

    private void createPDF() {
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_gubbi_logo);
        Bitmap checkBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check);

        new CreatePDFTask(clientHandovers, signatureImagePath,
                editRemarks.getText().toString().trim(), logoBitmap, checkBitmap, this).execute();
    }

    private void showPDF() {
        if (getContext() != null) {
            File filePath = new File(Environment.getExternalStorageDirectory(), File.separator + "GubbiConnect" + File.separator + "Reports");
            File handoverFile = new File(filePath, "Completion_Report.pdf");
            Uri contentUri = getUriForFile(getContext(), ClientConnectConstants.FILE_PROVIDER_AUTHORITY, handoverFile);

            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(contentUri, "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(target, "Open With");
            getContext().startActivity(intent);
        }
    }

    private void clearCache() {
        editRemarks.setText("");
        signatureImagePath = "";

        if (getContext() != null) {
            ContextWrapper contextWrapper = new ContextWrapper(getContext());
            File filesDir = contextWrapper.getDir("clientImages", Context.MODE_PRIVATE);
            if (filesDir.isDirectory()) {
                String[] subDir = filesDir.list();
                for (String dir : subDir) {
                    boolean result = new File(filesDir, dir).delete();
                    Log.d(TAG, "clearCache: " + result);
                }
            }
        }
    }

}