package com.mygubbi.imaginestclientconnect.clientDocuments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.DownloadManagerReceiver;
import com.mygubbi.imaginestclientconnect.helpers.ZipUtils;
import com.mygubbi.imaginestclientconnect.models.ClientDocument;
import com.mygubbi.imaginestclientconnect.models.DataEvent;
import com.mygubbi.imaginestclientconnect.models.UnzippedFiles;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.support.v4.content.FileProvider.getUriForFile;
import static com.mygubbi.imaginestclientconnect.helpers.ClientConnectHelper.isExternalCardMounted;

/**
 * @author Hemanth
 * @since 3/28/2018
 */
public class ClientDocumentsFragment extends Fragment implements ClientDocumentsView,
        ClientDocumentsAdapter.DocumentClickListener, ZipUtils.ZipUtilsListener {

    private static final String TAG = "ClientDocumentsFragment";

    private View rootView;

    private TextView textDocumentsCount;
    private RecyclerView documentsRecyclerView;

    private ArrayList<ClientDocument> documentsList;
    private ClientDocumentsAdapter documentsAdapter;
    private ClientDocumentsPresenter documentsPresenter;

    private ProgressDialog progressDialog;

    private int selectedDocumentPosition = -1;
    private ClientDocument selectedDocument;

    private DownloadManagerReceiver receiver;
    private ArrayList<Long> activeDownloadIds;
    private Set<Long> completedDownloadIds;
    private File file, zipTempFile;

    private static final int REQUEST_STORAGE = 489;
    private static final String directoryName = "GubbiConnect";
    private static final String documentsDir = "Documents";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        documentsPresenter = new ClientDocumentsPresenter(this);

        documentsList = new ArrayList<>();
        activeDownloadIds = new ArrayList<>();
        completedDownloadIds = new HashSet<>();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_documents, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textDocumentsCount = rootView.findViewById(R.id.text_documents_count);

        documentsRecyclerView = rootView.findViewById(R.id.recycler_view_documents);
        documentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        documentsAdapter = new ClientDocumentsAdapter(getContext(), documentsList, this);
        documentsRecyclerView.setAdapter(documentsAdapter);
        documentsRecyclerView.setNestedScrollingEnabled(false);

        documentsPresenter.getDocumentsList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkDownloadStatus();
    }

    @Override
    public void onStop() {
        if (receiver != null) {
            receiver.unregister(getContext());
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        super.onStop();
    }

    @SuppressLint("LogNotTimber")
    private void showFiles(boolean isExtracted, ArrayList<UnzippedFiles> unzippedFiles) {
        if (getContext() == null)
            return;

        final Dialog dialog = new Dialog(getContext());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_unzipped_files, null);

        ListView listView = dialogView.findViewById(R.id.list_unzipped_files);
        TextView textClose = dialogView.findViewById(R.id.textClose);

        UnzippedFilesAdapter filesAdapter = new UnzippedFilesAdapter(getContext(), unzippedFiles);

        listView.setAdapter(filesAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            UnzippedFiles unzippedFile = (UnzippedFiles) filesAdapter.getItem(position);

            String filePath = unzippedFile.getFilePath();
            String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());

            if (TextUtils.isEmpty(fileExtension)) {
                return;
            }

            File file = new File(filePath);

            if (fileExtension.equalsIgnoreCase("pdf")) {
                Uri contentUri = getUriForFile(getContext(), ClientConnectConstants.FILE_PROVIDER_AUTHORITY, file);

                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(contentUri, "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent intent = Intent.createChooser(target, "Open With");
                if (getContext() != null) {
                    getContext().startActivity(intent);
                }
            } else if (fileExtension.equalsIgnoreCase("jpg")
                    || fileExtension.equalsIgnoreCase("jpeg")
                    || fileExtension.equalsIgnoreCase("png")) {
                Uri contentUri = getUriForFile(getContext(), ClientConnectConstants.FILE_PROVIDER_AUTHORITY, file);

                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(contentUri, "image/*");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent intent = Intent.createChooser(target, "Open With");
                if (getContext() != null) {
                    getContext().startActivity(intent);
                }
            }
        });

        textClose.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(dialogView);
        dialog.show();
    }

    private ArrayList<UnzippedFiles> getFilesFromFolder(File zipTempFile) {
        ArrayList<UnzippedFiles> tempExtractedFiles = new ArrayList<>();
        if (zipTempFile.exists()) {
            File[] files = zipTempFile.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    getFilesFromFolder(file);
                } else {
                    tempExtractedFiles.add(new UnzippedFiles(file.getName(), file.getAbsolutePath()));
                }
            }
        }
        return tempExtractedFiles;
    }

    private ArrayList<ClientDocument> checkFilesDownloaded(ArrayList<ClientDocument> documentsList) {
        ArrayList<ClientDocument> tempList = new ArrayList<>();
        File directory;
        if (isExternalCardMounted()) {
            directory = new File(Environment.getExternalStorageDirectory() +
                    File.separator + directoryName);
        } else {
            directory = new File(Environment.DIRECTORY_DOWNLOADS +
                    File.separator + directoryName);
        }

        for (ClientDocument clientDocument : documentsList) {
            clientDocument.setFileDownloaded(traverseFiles(directory, clientDocument.getDocumentName()));
            clientDocument.setDownloadStatus(DataEvent.DOWNLOAD_STATUS);
            tempList.add(clientDocument);
        }

        return tempList;
    }

    private boolean traverseFiles(File directory, String fileName) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            boolean isFileDownload = false;
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        traverseFiles(file, fileName);
                    } else {
                        fileName = fileName.split("\\.")[0];

                        String existingFileName = file.getName().split("\\.")[0];
                        isFileDownload = existingFileName.equals(fileName);

                        if (isFileDownload) {
                            break;
                        }
                    }
                }
            }
            return isFileDownload;
        } else {
            return false;
        }
    }

    private void checkStoragePermissionAndDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext() != null && getActivity() != null) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    downloadFile(selectedDocument.getFileExt(), selectedDocument.getDocumentName(), selectedDocument.getDocUrl());
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

    private void downloadFile(String fileType, String filename, String fileUrl) {
        if (getContext() != null) {
            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl.replaceAll(" ", "%20")));

            String mimeType;

            switch (fileType) {
                case "zip":
                    mimeType = "application/zip";
                    break;
                case "pdf":
                    mimeType = "application/pdf";
                    break;
                default:
                    mimeType = "application/zip";
            }

            if (isExternalCardMounted()) {
                File direct = new File(Environment.getExternalStorageDirectory() + File.separator + directoryName);

                if (!direct.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    direct.mkdirs();
                }

                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("GubbiConnect")
                        .setDescription(filename)
                        .setMimeType(mimeType)
                        .setDestinationInExternalPublicDir(File.separator + directoryName, filename);
            } else {
                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("GubbiConnect")
                        .setDescription(filename)
                        .setMimeType(mimeType)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS
                                + File.separator + directoryName, filename);
            }

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);

            if (downloadManager != null) {
                long downloadId = downloadManager.enqueue(request);
                activeDownloadIds.add(downloadId);
                Toast.makeText(getContext(), "Download started", Toast.LENGTH_SHORT).show();

                checkDownloadStatus();

                receiver = new DownloadManagerReceiver(downloadManager);
                receiver.register(getContext(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private void checkDownloadStatus() {
        if (getContext() != null && !activeDownloadIds.isEmpty()) {
            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                completedDownloadIds.clear();

                DownloadManager.Query query = new DownloadManager.Query();
                for (Long downloadId : activeDownloadIds) {
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        //column for status
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int status = cursor.getInt(columnIndex);
                        //column for reason code if the download failed or paused
                        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                        int reason = cursor.getInt(columnReason);

                        int downloadedBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        final int downloadProgress = (int) ((downloadedBytes * 100L) / totalBytes);

                        String statusText = "";
                        String reasonText = "";

                        switch (status) {
                            case DownloadManager.STATUS_FAILED:
                                statusText = "Download Failed";
                                switch (reason) {
                                    case DownloadManager.ERROR_CANNOT_RESUME:
                                        reasonText = "Error cannot resume";
                                        break;
                                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                                        reasonText = "Error device not found";
                                        break;
                                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                                        reasonText = "Error file already exists";
                                        break;
                                    case DownloadManager.ERROR_FILE_ERROR:
                                        reasonText = "Error file error";
                                        break;
                                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                                        reasonText = "Error http data error";
                                        break;
                                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                                        reasonText = "Error insufficient space";
                                        break;
                                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                                        reasonText = "Error too many redirects";
                                        break;
                                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                                        reasonText = "Error unhandled http code";
                                        break;
                                    case DownloadManager.ERROR_UNKNOWN:
                                        reasonText = "Error unknown";
                                        break;
                                }

                                updateDownloadStatus(DataEvent.DOWNLOAD_FAILED, statusText + ". " + reasonText, 0);
                                break;
                            case DownloadManager.STATUS_PAUSED:
                                statusText = "Download Paused";
                                switch (reason) {
                                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                                        reasonText = "Paused queued for wifi";
                                        break;
                                    case DownloadManager.PAUSED_UNKNOWN:
                                        reasonText = "Paused unknown";
                                        break;
                                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                                        reasonText = "Paused waiting for network";
                                        break;
                                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                                        reasonText = "Paused waiting to retry";
                                        break;
                                }

                                updateDownloadStatus(DataEvent.DOWNLOAD_PAUSED, statusText + ". " + reasonText, 0);
                                break;
                            case DownloadManager.STATUS_PENDING:
                                statusText = "Download pending";
                                updateDownloadStatus(DataEvent.DOWNLOAD_IN_PROGRESS, "", downloadProgress);
                                break;
                            case DownloadManager.STATUS_RUNNING:
                                statusText = "Download running";
                                updateDownloadStatus(DataEvent.DOWNLOAD_IN_PROGRESS, "", downloadProgress);
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                statusText = "Download successful";
                                updateDownloadStatus(DataEvent.DOWNLOAD_SUCCESS, statusText, 0);

                                completedDownloadIds.add(downloadId);
                                break;
                        }
                        Log.d(TAG, "checkDownloadStatus: " + statusText);
                    }
                }

                for (Long completedDownloadId : completedDownloadIds) {
                    activeDownloadIds.remove(completedDownloadId);
                }
            }
        }
    }

    private void updateDownloadStatus(String downloadStatus, @Nullable String message, int progress) {
        if (selectedDocumentPosition == -1)
            return;

        ClientDocument clientDocument;
        switch (downloadStatus) {
            case DataEvent.DOWNLOAD_IN_PROGRESS:
                clientDocument = documentsList.get(selectedDocumentPosition);
                clientDocument.setDownloadStatus(DataEvent.DOWNLOAD_IN_PROGRESS);

                documentsList.set(selectedDocumentPosition, clientDocument);
                documentsAdapter.notifyDataSetChanged();
                break;
            case DataEvent.DOWNLOAD_SUCCESS:
                clientDocument = documentsList.get(selectedDocumentPosition);
                clientDocument.setDownloadStatus(DataEvent.DOWNLOAD_SUCCESS);
                clientDocument.setFileDownloaded(true);

                documentsList.set(selectedDocumentPosition, clientDocument);
                documentsAdapter.notifyDataSetChanged();

                onItemClick(selectedDocumentPosition);
                break;
            case DataEvent.DOWNLOAD_PAUSED:
                clientDocument = documentsList.get(selectedDocumentPosition);
                clientDocument.setDownloadStatus(DataEvent.DOWNLOAD_PAUSED);
                documentsList.set(selectedDocumentPosition, clientDocument);
                documentsAdapter.notifyDataSetChanged();
                break;
            case DataEvent.DOWNLOAD_FAILED:
                clientDocument = documentsList.get(selectedDocumentPosition);
                clientDocument.setDownloadStatus(DataEvent.DOWNLOAD_FAILED);
                documentsList.set(selectedDocumentPosition, clientDocument);
                documentsAdapter.notifyDataSetChanged();

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void showDocuments(ArrayList<ClientDocument> documentsList) {
        if (!documentsList.isEmpty()) {
            this.documentsList.clear();
            this.documentsList.addAll(checkFilesDownloaded(documentsList));
            final String documentCount = "Client documents (" + documentsList.size() + ")";
            textDocumentsCount.setText(documentCount);
            documentsAdapter.notifyDataSetChanged();
        } else {
            rootView.findViewById(R.id.text_no_data).setVisibility(View.VISIBLE);
            textDocumentsCount.setVisibility(View.GONE);
            documentsRecyclerView.setVisibility(View.GONE);
        }
        hideProgress();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (selectedDocument != null) {
                downloadFile(selectedDocument.getFileExt(), selectedDocument.getDocumentName(), selectedDocument.getDocUrl());
            } else {
                Toast.makeText(getContext(), "Failed to download file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Storage permission denied", Toast.LENGTH_SHORT).show();
            // We were not granted permission this time, so don't try to download the file
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onItemClick(int position) {
        ClientDocument selectedDocument = documentsList.get(position);

        if (selectedDocument.isFileDownloaded()) {
            if (getContext() == null)
                return;

            if (isExternalCardMounted()) {
                file = new File(Environment.getExternalStorageDirectory()
                        + File.separator + directoryName
                        + File.separator + selectedDocument.getDocumentName());

                zipTempFile = new File(Environment.getExternalStorageDirectory()
                        + File.separator + directoryName
                        + File.separator + documentsDir
                        + File.separator + selectedDocument.getDocumentName().split("\\.")[0]);
            } else {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator + directoryName
                        + File.separator + selectedDocument.getDocumentName());

                zipTempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator + directoryName
                        + File.separator + documentsDir
                        + File.separator + selectedDocument.getDocumentName().split("\\.")[0]);
            }

            if (file.exists()) {
                if (selectedDocument.getFileExt().equalsIgnoreCase("pdf")) {
                    Uri contentUri = getUriForFile(getContext(), ClientConnectConstants.FILE_PROVIDER_AUTHORITY, file);

                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(contentUri, "application/pdf");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    Intent intent = Intent.createChooser(target, "Open With");
                    if (getContext() != null) {
                        getContext().startActivity(intent);
                    }
                } else if (selectedDocument.getFileExt().equalsIgnoreCase("zip")) {
                    if (zipTempFile.exists()) {
                        showFiles(true, getFilesFromFolder(zipTempFile));
                    } else {
                        new ZipUtils.unZip(file, zipTempFile, ClientDocumentsFragment.this).execute();
                    }
                } else if (selectedDocument.getFileExt().equalsIgnoreCase("jpg")
                        || selectedDocument.getFileExt().equalsIgnoreCase("jpeg")
                        || selectedDocument.getFileExt().equalsIgnoreCase("png")) {
                    Uri contentUri = getUriForFile(getContext(), ClientConnectConstants.FILE_PROVIDER_AUTHORITY, file);

                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(contentUri, "image/*");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    Intent intent = Intent.createChooser(target, "Open With");
                    if (getContext() != null) {
                        getContext().startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    public void onDownloadClick(int position) {
        selectedDocumentPosition = position;
        selectedDocument = documentsList.get(position);

        if (!selectedDocument.isFileDownloaded()) {
            checkStoragePermissionAndDownload();
        } else {
            onItemClick(position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent(DataEvent event) {
        switch (event.getKey()) {
            case DataEvent.DOWNLOAD_SUCCESS:
                updateDownloadStatus(DataEvent.DOWNLOAD_SUCCESS, event.getValue(), 0);
                break;
            case DataEvent.DOWNLOAD_IN_PROGRESS:
                updateDownloadStatus(DataEvent.DOWNLOAD_IN_PROGRESS, event.getValue(), 0);
                break;
            case DataEvent.DOWNLOAD_FAILED:
                updateDownloadStatus(DataEvent.DOWNLOAD_FAILED, event.getValue(), 0);
                break;
        }
    }

    @Override
    public void unZippingStarted() {
        showProgress("Extracting zip files...");
    }

    @Override
    public void zipFileContents(List<String> fileNamesList) {
        hideProgress();
//        showFiles(false, (ArrayList<String>) fileNamesList, null);
    }

    @Override
    public void unZipSuccess(File zipTempFile) {
        hideProgress();
        showFiles(true, getFilesFromFolder(zipTempFile));
    }

    @Override
    public void unZipFailed() {
        hideProgress();
    }

}