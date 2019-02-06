package com.mygubbi.imaginestclientconnect.clientFeedback;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectActivity;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectApplication;
import com.mygubbi.imaginestclientconnect.helpers.OnFragmentInteractionListener;
import com.mygubbi.imaginestclientconnect.models.ClientFeedBackResponse;
import com.mygubbi.imaginestclientconnect.models.FeedbackQuestionGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hemanth
 * @since 4/24/2018
 */
public class ClientFeedbackFragment extends Fragment implements ClientFeedbackView,
        FeedbackQuestionAdapter.FeedbackInteractionListener {

//    private static final String TAG = "ClientFeedbackFragment";

    private View rootView;

    private ProgressDialog progressDialog;

    private ClientFeedbackPresenter clientFeedbackPresenter;

    private Map<String, ClientFeedBackResponse> feedBackResponseMap;

    private OnFragmentInteractionListener listener;

    String type="";
    String stage="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        clientFeedbackPresenter = new ClientFeedbackPresenter(this);

        feedBackResponseMap = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feedback, container, false);

        /*String  value = getArguments().getString("feedback");
        System.out.println("data passing between fragments "+value);*/

        return rootView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        type= ClientConnectActivity.feedbackType;

        stage=ClientConnectActivity.salesStage;

        System.out.println("test feedback type :: "+type +"  "+"stage::"+stage);

        if(type.equals("feedback")){
        clientFeedbackPresenter.getFeedbackQuestions("handover");
        }
        else if(type.equals("Dsofeedback")){

            clientFeedbackPresenter.getFeedbackQuestions("dso");

        }
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
    public void showFeedbackQuestion(List<FeedbackQuestionGroup> feedbackQuestionGroups) {
        RecyclerView recyclerViewQuestions = rootView.findViewById(R.id.recycler_view_feedback);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        FeedbackQuestionAdapter questionAdapter = new FeedbackQuestionAdapter(getContext(), feedbackQuestionGroups, this);
        recyclerViewQuestions.setAdapter(questionAdapter);

        hideProgress();
    }

    @Override
    public void showFeedbackSubmitted(boolean result) {
        if (result) {
            if (getContext() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Success")
                        .setMessage("Thank you for your valuable feedback")
                        .setCancelable(false)
                        .setPositiveButton("Close", (dialog, which) -> {
                            dialog.dismiss();
                            listener.onBackClick();
                        });

                Dialog dialog = builder.create();
                dialog.show();
            }
        } else {
            Toast.makeText(getContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onAnswered(String questionCode, String answerCode,String questionGroupCode) {
        feedBackResponseMap.put(questionCode,
                new ClientFeedBackResponse(ClientConnectApplication.getInstance().getOpportunityId(), questionCode, answerCode,questionGroupCode));
    }

    @Override
    public void onSubmitClick() {
        if (!feedBackResponseMap.isEmpty()) {

            if(type.equals("feedback")){
                Gson gson = new Gson();
                String jsonArray = gson.toJson(new ArrayList<>(feedBackResponseMap.values()));
                System.out.println("list of json array ::----::"+jsonArray);
                clientFeedbackPresenter.submitUserFeedback(new ArrayList<>(feedBackResponseMap.values()));
                }
            else if(type.equals("Dsofeedback")){
                clientFeedbackPresenter.submitUserFeedback(new ArrayList<>(feedBackResponseMap.values()));
                }

        } else {
            Toast.makeText(getContext(), "Please fill the feedback form", Toast.LENGTH_SHORT).show();
        }
    }
}