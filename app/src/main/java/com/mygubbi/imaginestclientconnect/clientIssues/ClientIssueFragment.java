package com.mygubbi.imaginestclientconnect.clientIssues;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientIssues.activities.ClientIssueDetailActivity;
import com.mygubbi.imaginestclientconnect.clientIssues.adapters.ClientIssuesAdapter;
import com.mygubbi.imaginestclientconnect.helpers.ClientConnectConstants;
import com.mygubbi.imaginestclientconnect.helpers.OnFragmentInteractionListener;
import com.mygubbi.imaginestclientconnect.helpers.RecyclerTouchListener;
import com.mygubbi.imaginestclientconnect.helpers.RecyclerViewClickListener;
import com.mygubbi.imaginestclientconnect.models.ClientIssue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hemanth
 * @since 3/27/2018
 */
public class ClientIssueFragment extends Fragment implements ClientIssueView {

    private static final String TAG = "ClientIssueFragment";

    private View rootView;
    private LinearLayout layoutIssuesMain;
    private TextView textOpenIssuesHeader, textClosedIssuesHeader, textNoData;
    private ImageView imageOpenIssuesExpand, imageClosedIssuesExpand;
    private RecyclerView recyclerViewOpenIssues, recyclerViewClosedIssues;

    private ProgressDialog progressDialog;

    private ClientIssuePresenter clientSupportPresenter;

    private OnFragmentInteractionListener listener;

    private ArrayList<ClientIssue> openIssuesList, completedIssuesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        openIssuesList = new ArrayList<>();
        completedIssuesList = new ArrayList<>();
        clientSupportPresenter = new ClientIssuePresenter(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_issues, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RelativeLayout layoutOpenIssues = rootView.findViewById(R.id.layout_open_issues);
        RelativeLayout layoutClosedIssues = rootView.findViewById(R.id.layout_closed_issues);

        layoutIssuesMain = rootView.findViewById(R.id.layout_issues_main);

        textNoData = rootView.findViewById(R.id.text_no_data);
        textOpenIssuesHeader = rootView.findViewById(R.id.text_open_issues_header);
        textClosedIssuesHeader = rootView.findViewById(R.id.text_closed_issues_header);
        imageOpenIssuesExpand = rootView.findViewById(R.id.image_open_issues_expand);
        imageClosedIssuesExpand = rootView.findViewById(R.id.image_closed_issues_expand);

        recyclerViewOpenIssues = rootView.findViewById(R.id.recycler_view_open_issues);
        recyclerViewOpenIssues.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerViewClosedIssues = rootView.findViewById(R.id.recycler_view_closed_issues);
        recyclerViewClosedIssues.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerViewOpenIssues.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerViewOpenIssues,
                new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ClientIssue issue = openIssuesList.get(position);
                        showIssueDetails(issue);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        recyclerViewClosedIssues.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerViewOpenIssues,
                new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ClientIssue issue = completedIssuesList.get(position);
                        showIssueDetails(issue);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        layoutOpenIssues.setOnClickListener(v -> {
            if (recyclerViewOpenIssues.getVisibility() == View.VISIBLE) {
                recyclerViewOpenIssues.setVisibility(View.GONE);
                imageOpenIssuesExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
            } else {
                recyclerViewOpenIssues.setVisibility(View.VISIBLE);
                recyclerViewClosedIssues.setVisibility(View.GONE);
                imageOpenIssuesExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
            }
        });

        layoutClosedIssues.setOnClickListener(v -> {
            if (recyclerViewClosedIssues.getVisibility() == View.VISIBLE) {
                recyclerViewClosedIssues.setVisibility(View.GONE);
                imageClosedIssuesExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
            } else {
                recyclerViewClosedIssues.setVisibility(View.VISIBLE);
                recyclerViewOpenIssues.setVisibility(View.GONE);
                imageClosedIssuesExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
            }
        });
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
    public void onResume() {
        super.onResume();
        listener.onFragmentVisible();
        getClientSupportList();
    }

    @Override
    public void onPause() {
        listener.onFragmentRemoved();
        super.onPause();
    }

    private void showIssueDetails(ClientIssue issue) {
        Intent intent = new Intent(getContext(), ClientIssueDetailActivity.class);
        intent.putExtra(ClientConnectConstants.CLIENT_ISSUE, issue);
        startActivity(intent);
    }

    public void getClientSupportList() {
        clientSupportPresenter.getClientSupportList();
    }

    @Override
    public void showClientSupportDetails(List<ClientIssue> openIssuesList, List<ClientIssue> completedIssuesList) {
        if (openIssuesList.isEmpty() && completedIssuesList.isEmpty()) {
            layoutIssuesMain.setVisibility(View.GONE);
            textNoData.setVisibility(View.VISIBLE);
        } else {
            layoutIssuesMain.setVisibility(View.VISIBLE);
            textNoData.setVisibility(View.GONE);

            this.openIssuesList = (ArrayList<ClientIssue>) openIssuesList;
            this.completedIssuesList = (ArrayList<ClientIssue>) completedIssuesList;

            String openIssuesCount = "Open issues (" + openIssuesList.size() + ")";
            textOpenIssuesHeader.setText(openIssuesCount);
            String closedIssuesCount = "Completed issues (" + this.completedIssuesList.size() + ")";
            textClosedIssuesHeader.setText(closedIssuesCount);

            ClientIssuesAdapter openIssuesAdapter = new ClientIssuesAdapter(getContext(), this.openIssuesList);
            ClientIssuesAdapter closedIssuesAdapter = new ClientIssuesAdapter(getContext(), this.completedIssuesList);

            recyclerViewOpenIssues.setAdapter(openIssuesAdapter);
            recyclerViewClosedIssues.setAdapter(closedIssuesAdapter);

            recyclerViewOpenIssues.setHasFixedSize(true);
            recyclerViewClosedIssues.setHasFixedSize(true);

            recyclerViewOpenIssues.setNestedScrollingEnabled(false);
            recyclerViewClosedIssues.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public void onSupportCreated(String message) {
    }

    @Override
    public void onSupportCreationFailed() {

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