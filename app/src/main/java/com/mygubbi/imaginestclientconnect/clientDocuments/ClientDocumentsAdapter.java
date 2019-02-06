package com.mygubbi.imaginestclientconnect.clientDocuments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.models.ClientDocument;
import com.mygubbi.imaginestclientconnect.models.DataEvent;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 3/28/2018
 */
public class ClientDocumentsAdapter extends RecyclerView.Adapter<ClientDocumentsAdapter.DocumentViewHolder> {

//    private static final String TAG = "DocumentsAdapter";

    private Context context;
    private ArrayList<ClientDocument> dataList;
    private DocumentClickListener listener;

    ClientDocumentsAdapter(Context context, ArrayList<ClientDocument> dataList, DocumentClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int index) {
        final int position = holder.getAdapterPosition();

        ClientDocument document = dataList.get(position);

        holder.textDocumentTitle.setText(document.getDocumentName());
        holder.textDocumentDescription.setText(document.getDocCategory());

        if (document.getFileExt().equalsIgnoreCase("pdf")) {
            holder.imageDocumentType.setImageResource(R.drawable.ic_pdf);
        } else if (document.getFileExt().equalsIgnoreCase("doc") || document.getFileExt().equalsIgnoreCase("docx")) {
            holder.imageDocumentType.setImageResource(R.drawable.ic_doc);
        } else if (document.getFileExt().equalsIgnoreCase("xls") || document.getFileExt().equalsIgnoreCase("xlsx")) {
            holder.imageDocumentType.setImageResource(R.drawable.ic_xls);
        } else if (document.getFileExt().equalsIgnoreCase("zip") || document.getFileExt().equalsIgnoreCase("rar")) {
            holder.imageDocumentType.setImageResource(R.drawable.ic_zip);
        } else {
            holder.imageDocumentType.setImageResource(R.drawable.ic_others);
        }

        switch (document.getDownloadStatus()) {
            case DataEvent.DOWNLOAD_IN_PROGRESS:
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.progressBar.setProgress(document.getDownloadProgress());

                holder.imageDocumentDownload.setVisibility(View.GONE);
                break;
            case DataEvent.DOWNLOAD_SUCCESS:
                holder.progressBar.setVisibility(View.GONE);
                holder.imageDocumentDownload.setVisibility(View.VISIBLE);
                holder.imageDocumentDownload.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            default:
                holder.progressBar.setVisibility(View.GONE);
                holder.imageDocumentDownload.setVisibility(View.VISIBLE);
                holder.imageDocumentDownload.setColorFilter(ContextCompat.getColor(context, R.color.gray),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                break;
        }

        if (document.isFileDownloaded()) {
            holder.imageDocumentDownload.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            holder.imageDocumentDownload.setColorFilter(ContextCompat.getColor(context, R.color.gray),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageDocumentType, imageDocumentDownload;
        private TextView textDocumentTitle, textDocumentDescription;
        private ProgressBar progressBar;

        DocumentViewHolder(View itemView) {
            super(itemView);

            imageDocumentType = itemView.findViewById(R.id.image_document_type);
            imageDocumentDownload = itemView.findViewById(R.id.image_document_download);
            textDocumentTitle = itemView.findViewById(R.id.text_document_title);
            textDocumentDescription = itemView.findViewById(R.id.text_document_description);
            progressBar = itemView.findViewById(R.id.progress_bar_documents);

            imageDocumentDownload.setOnClickListener(v -> listener.onDownloadClick(getAdapterPosition()));

            itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition()));
        }
    }

    public interface DocumentClickListener {

        void onItemClick(int position);

        void onDownloadClick(int position);
    }
}