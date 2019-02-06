package com.mygubbi.imaginestclientconnect.clientIssues.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectHelper;
import com.mygubbi.imaginestclientconnect.helpers.GlideApp;
import com.mygubbi.imaginestclientconnect.models.ClientIssue;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 3/27/2018
 */
public class ClientIssuesAdapter extends RecyclerView.Adapter<ClientIssuesAdapter.SupportViewHolder> {

    private static final String TAG = "IssuesAdapter";

    private Context context;
    private ArrayList<ClientIssue> dataList;

    public ClientIssuesAdapter(Context context, ArrayList<ClientIssue> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SupportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_issue, parent, false);
        return new SupportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportViewHolder holder, int index) {
        final int position = holder.getAdapterPosition();

        ClientIssue issue = dataList.get(position);

        holder.textIssueTitle.setText(ClientConnectHelper.upperCaseFirst(issue.getName()));

        String dueDateString = issue.getDueDateC() == null
                || issue.getDueDateC().equalsIgnoreCase("null") ? "" : issue.getDueDateC();

        String dueDate = "Due date : " + dueDateString;

        holder.textIssueDueDate.setText(dueDate);

        if (!TextUtils.isEmpty(issue.getDocumentUrl())) {
            GlideApp.with(context)
                    .load(issue.getDocumentUrl())
                    .centerCrop()
                    .into(holder.imageIssuePhotos);

            holder.divider.setVisibility(View.VISIBLE);
            holder.imageIssuePhotos.setVisibility(View.VISIBLE);
            holder.textIssuePhotosCount.setVisibility(View.VISIBLE);
        } else {
            holder.divider.setVisibility(View.GONE);
            holder.imageIssuePhotos.setVisibility(View.GONE);
            holder.textIssuePhotosCount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class SupportViewHolder extends RecyclerView.ViewHolder {

        private View divider;
        private ImageView imageIssuePhotos;
        private TextView textIssueTitle, textIssueDueDate, textIssuePhotosCount;

        SupportViewHolder(View itemView) {
            super(itemView);

            divider = itemView.findViewById(R.id.divider);
            imageIssuePhotos = itemView.findViewById(R.id.image_issue_photo);
            textIssueTitle = itemView.findViewById(R.id.text_issue_title);
            textIssueDueDate = itemView.findViewById(R.id.text_issue_due_date);
            textIssuePhotosCount = itemView.findViewById(R.id.text_image_count);
        }
    }
}