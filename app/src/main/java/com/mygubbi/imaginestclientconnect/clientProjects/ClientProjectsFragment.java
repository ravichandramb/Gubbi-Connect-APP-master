package com.mygubbi.imaginestclientconnect.clientProjects;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.models.ClientProject;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 3/28/2018
 */
public class ClientProjectsFragment extends Fragment implements ClientProjectsView {

    private View rootView;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClientProjectsPresenter clientUpdatesPresenter = new ClientProjectsPresenter(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        clientUpdatesPresenter.getClientProjectUpdates();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_projects, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showProjectUpdates(ArrayList<ClientProject> updatesList) {
        RecyclerView updatesRecyclerView = rootView.findViewById(R.id.recycler_view_updates);
        if (!updatesList.isEmpty()) {
            updatesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            ClientProjectsAdapter updatesAdapter = new ClientProjectsAdapter(getContext(), updatesList);
            updatesRecyclerView.setAdapter(updatesAdapter);

            updatesRecyclerView.setNestedScrollingEnabled(false);
        } else {
            rootView.findViewById(R.id.text_no_data).setVisibility(View.VISIBLE);
            updatesRecyclerView.setVisibility(View.GONE);
        }
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