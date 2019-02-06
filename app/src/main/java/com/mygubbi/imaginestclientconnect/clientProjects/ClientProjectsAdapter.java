package com.mygubbi.imaginestclientconnect.clientProjects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.helpers.CircularProgressView;
import com.mygubbi.imaginestclientconnect.models.ClientProject;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 3/28/2018
 */
public class ClientProjectsAdapter extends RecyclerView.Adapter<ClientProjectsAdapter.ProjectsViewHolder> {

    private Context context;
    private ArrayList<ClientProject> dataList;

    ClientProjectsAdapter(Context context, ArrayList<ClientProject> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_update, parent, false);
        return new ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsViewHolder holder, int index) {
        final int position = holder.getAdapterPosition();

        ClientProject update = dataList.get(position);

        holder.textProject.setText(update.getName());

        String status;

        if (update.getStatus() == 0) {
            status = "Not started";
            holder.circularProgressView.setProgress(0);
            holder.circularProgressView.setProgressColor(ContextCompat.getColor(context, R.color.progress_red));
            holder.imageViewStatus.setImageResource(R.drawable.ic_not_started);
            holder.viewStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.progress_red));
        } else if (update.getStatus() == 1) {
            status = "In progress";
            holder.circularProgressView.setProgress(50);
            holder.circularProgressView.setProgressColor(ContextCompat.getColor(context, R.color.progress_orange));
            holder.imageViewStatus.setImageResource(R.drawable.ic_in_progress);
            holder.viewStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.progress_orange));
        } else if (update.getStatus() == 2) {
            status = "Completed";
            holder.circularProgressView.setProgress(100);
            holder.circularProgressView.setProgressColor(ContextCompat.getColor(context, R.color.progress_green));
            holder.imageViewStatus.setImageResource(R.drawable.ic_check_done);
            holder.viewStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.progress_green));
        } else {
            status = "";
            holder.circularProgressView.setProgress(0);
            holder.circularProgressView.setProgressColor(ContextCompat.getColor(context, R.color.progress_red));
            holder.imageViewStatus.setImageResource(R.drawable.ic_not_started);
            holder.viewStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.progress_red));
        }

        holder.textProjectStatus.setText(status);

        holder.circularProgressView.bringToFront();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ProjectsViewHolder extends RecyclerView.ViewHolder {

        private TextView textProject, textProjectStatus;
        private CircularProgressView circularProgressView;
        private ImageView imageViewStatus;
        private View viewStatus;

        ProjectsViewHolder(View itemView) {
            super(itemView);

            textProject = itemView.findViewById(R.id.text_project);
            textProjectStatus = itemView.findViewById(R.id.text_project_status);
            circularProgressView = itemView.findViewById(R.id.circular_progress_view);
            imageViewStatus = itemView.findViewById(R.id.image_view_status);
            viewStatus = itemView.findViewById(R.id.view_status);
        }
    }
}