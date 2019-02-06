package com.mygubbi.imaginestclientconnect.clientConnectMain.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mygubbi.imaginestclientconnect.R;
import com.mygubbi.imaginestclientconnect.clientConnectMain.ClientConnectActivity;
import com.mygubbi.imaginestclientconnect.clientContacts.ClientContactsFragment;
import com.mygubbi.imaginestclientconnect.clientDocuments.ClientDocumentsFragment;
import com.mygubbi.imaginestclientconnect.clientFeedback.ClientFeedbackFragment;
import com.mygubbi.imaginestclientconnect.clientHandover.ClientHandoverFragment;
import com.mygubbi.imaginestclientconnect.clientIssues.ClientIssueFragment;
import com.mygubbi.imaginestclientconnect.clientProjects.ClientProjectsFragment;
import com.mygubbi.imaginestclientconnect.helpers.OnFragmentInteractionListener;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;

/**
 * @author Hemanth
 * @since 3/29/2018
 */
public class ClientConnectFragment extends Fragment implements View.OnClickListener, ClientConnectView {

    private View rootView;
    private TextView textProfileName, textProfileAddress;
    Button button;
    private ProgressDialog progressDialog;
    private ClientConnectPresenter clientConnectPresenter;
    private OnFragmentInteractionListener listener;

    public ClientProfile clientProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientConnectPresenter = new ClientConnectPresenter(getContext(), this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_client_connect, container, false);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CardView cardContacts = rootView.findViewById(R.id.card_contacts);
        CardView cardDocuments = rootView.findViewById(R.id.card_documents);
        CardView cardUpdate = rootView.findViewById(R.id.card_projects);
        CardView cardSupport = rootView.findViewById(R.id.card_support);
        CardView cardHandover = rootView.findViewById(R.id.card_handover);
        CardView cardFeedBack = rootView.findViewById(R.id.card_feedback);
        TextView feedback=rootView.findViewById(R.id.text_feedback);



        textProfileName = rootView.findViewById(R.id.text_profile_name);
        textProfileAddress = rootView.findViewById(R.id.text_profile_address);

        cardContacts.setOnClickListener(this);
        cardDocuments.setOnClickListener(this);
        cardUpdate.setOnClickListener(this);
        cardSupport.setOnClickListener(this);
        cardHandover.setOnClickListener(this);
        cardFeedBack.setOnClickListener(this);

        getClientProfile();
      /*  if(ClientConnectActivity.salesStage.equalsIgnoreCase("INSTALLATION")||ClientConnectActivity.salesStage.equalsIgnoreCase("Project Handover")){
            feedback.setText("Feedback");
            }
        else{
            feedback.setText("Feedback");

        }*/


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
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.card_contacts:
                listener.onFragmentInteraction("Contacts", ClientContactsFragment.newInstance(clientProfile));
                break;
            case R.id.card_documents:
                listener.onFragmentInteraction("Documents", new ClientDocumentsFragment());
                break;
            case R.id.card_projects:
                listener.onFragmentInteraction("My Projects", new ClientProjectsFragment());
                break;
            case R.id.card_support:
                listener.onFragmentInteraction("Support", new ClientIssueFragment());
                break;
            case R.id.card_handover:
                listener.onFragmentInteraction("Handover", new ClientHandoverFragment());
                break;
            case R.id.card_feedback:

           /*   //  PopupMenu popup = new PopupMenu(getActivity(),card_feedback );
                Context wrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenu);

                final PopupMenu popup = new PopupMenu(*//*getActivity(),*//*wrapper,v,Gravity.START);
                popup.setGravity(Gravity.END);
                popup.getMenuInflater().inflate(R.menu.feedback_popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.feedback:
                                ClientConnectActivity.feedbackType="feedback";
                                boolean isFeedBackSubmitted = !TextUtils.isEmpty(ClientConnectFragment.this.clientProfile.getFeedbackSubmitted())
                        || (ClientConnectFragment.this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("No")
                        || ClientConnectFragment.this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("null"));

                if (isFeedBackSubmitted) {
                    listener.onFragmentInteraction("Feedback",new ClientFeedbackFragment());
                } else {
                    Toast.makeText(getContext(), "Feedback form has already submitted.", Toast.LENGTH_SHORT).show();
                }

                                break;
                            case R.id.dso_feedback:

                                ClientConnectActivity.feedbackType="Dsofeedback";

                                boolean isDsoFeedBackSubmitted = !TextUtils.isEmpty(ClientConnectFragment.this.clientProfile.getFeedbackSubmitted())
                                        || (ClientConnectFragment.this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("No")
                                        || ClientConnectFragment.this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("null"));
                                if (isDsoFeedBackSubmitted) {
                                    listener.onFragmentInteraction("Feedback",new ClientFeedbackFragment());
                                } else {
                                    Toast.makeText(getContext(), "DSO Feedback form has already submitted.", Toast.LENGTH_SHORT).show();
                                }


                                break;

                            default:
                                break;
                        }

                        return true;
                    }
                });
              //  setMenuBackground();
                popup.show();*/


if(ClientConnectActivity.salesStage.equalsIgnoreCase("INSTALLATION")||ClientConnectActivity.salesStage.equalsIgnoreCase("Project Handover")){
           boolean isFeedBackSubmitted = !TextUtils.isEmpty(this.clientProfile.getFeedbackSubmitted())
                        || (this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("No")
                        || this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("null"));

                System.out.println("check feedback test isEmpty "+ TextUtils.isEmpty(this.clientProfile.getFeedbackSubmitted()) +" test is NO :"
                        +this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("No") +" test is null :" +this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("null"));

                if (isFeedBackSubmitted) {
                    ClientConnectActivity.feedbackType="feedback";
                    listener.onFragmentInteraction("Feedback", new ClientFeedbackFragment());
                } else {
                    Toast.makeText(getContext(), "Feedback form has already submitted.", Toast.LENGTH_SHORT).show();
                }
}
else{

    boolean isFeedBackSubmitted = !TextUtils.isEmpty(this.clientProfile.getFeedbackSubmitted())
            || (this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("No")
            || this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("null"));

    System.out.println("check feedback test isEmpty "+ TextUtils.isEmpty(this.clientProfile.getFeedbackSubmitted()) +" test is NO :"
            +this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("No") +" test is null :" +this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("null"));

    if (isFeedBackSubmitted) {
        ClientConnectActivity.feedbackType="Dsofeedback";
        listener.onFragmentInteraction("Feedback", new ClientFeedbackFragment());
    } else {
        Toast.makeText(getContext(), "Dsofeedback form has already submitted.", Toast.LENGTH_SHORT).show();
    }
}

          /*      boolean isFeedBackSubmitted = !TextUtils.isEmpty(this.clientProfile.getFeedbackSubmitted())
                        || (this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("No")
                        || this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("null"));

                System.out.println("check feedback test isEmpty "+ TextUtils.isEmpty(this.clientProfile.getFeedbackSubmitted()) +" test is NO :"
                        +this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("No") +" test is null :" +this.clientProfile.getFeedbackSubmitted().equalsIgnoreCase("null"));

                if (isFeedBackSubmitted) {
                    ClientConnectActivity.feedbackType="feedback";
                    listener.onFragmentInteraction("Feedback", new ClientFeedbackFragment());
                } else {
                    Toast.makeText(getContext(), "Feedback form has already submitted.", Toast.LENGTH_SHORT).show();
                }*/
                break;
        }
    }

    protected void setMenuBackground(){
        // Log.d(TAG, "Enterting setMenuBackGround");


        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (inflater.getFactory() != null) {
            inflater = inflater.cloneInContext(getActivity());
        }
      //  inflater.setFactory(factory);

        inflater.setFactory( new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                        /* The background gets refreshed each time a new item is added the options menu.
                         * So each time Android applies the default background we need to set our own
                         * background. This is done using a thread giving the background change as runnable
                         * object */
                        new Handler().post(new Runnable() {
                            public void run () {
                                // sets the background color
                                view.setBackgroundResource( R.color.black);
                                // sets the text color
                                ((TextView) view).setTextColor(Color.WHITE);
                                // sets the text size
                                ((TextView) view).setTextSize(18);
                            }
                        } );
                        return view;
                    }
                    catch ( InflateException e ) {}
                    catch ( ClassNotFoundException e ) {}
                }
                return null;
            }});
    }


    private void getClientProfile() {
        clientConnectPresenter.getClientProfile();
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

    @Override
    public void showProfileData(ClientProfile clientProfile) {
        hideProgress();
        if (clientProfile != null) {
            textProfileName.setText(clientProfile.getCustomerName());
            textProfileAddress.setText(clientProfile.getCustomerPhone());
            listener.onProfileUpdated(clientProfile);
            this.clientProfile = clientProfile;
        }
    }
}