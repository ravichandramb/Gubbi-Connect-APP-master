package com.mygubbi.imaginestclientconnect.clientContacts;

import android.content.Intent;
import android.net.Uri;
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
import com.mygubbi.imaginestclientconnect.helpers.RecyclerTouchListener;
import com.mygubbi.imaginestclientconnect.helpers.RecyclerViewClickListener;
import com.mygubbi.imaginestclientconnect.models.ClientProfile;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 3/27/2018
 */
public class ClientContactsFragment extends Fragment {

//    private static final String TAG = "ClientContactsFragment";

    private View rootView;
    private ClientProfile clientProfile;

    public static ClientContactsFragment newInstance(ClientProfile clientProfile) {
        ClientContactsFragment clientProfileFragment = new ClientContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("clientProfile", clientProfile);
        clientProfileFragment.setArguments(bundle);
        return clientProfileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            clientProfile = bundle.getParcelable("clientProfile");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_client_contacts, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<ClientContact> contactsList = getList();

        if (contactsList != null && !contactsList.isEmpty()) {
            RecyclerView contactsRecyclerView = rootView.findViewById(R.id.recycler_view_contacts);
            contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            ClientContactsAdapter contactsAdapter = new ClientContactsAdapter(contactsList);
            contactsRecyclerView.setAdapter(contactsAdapter);
            contactsRecyclerView.setNestedScrollingEnabled(false);

            contactsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), contactsRecyclerView,
                    new RecyclerViewClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                            dialIntent.setData(Uri.parse("tel:" + contactsList.get(position).getPhone()));
                            startActivity(dialIntent);
                        }

                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));
        }

    }

    public ArrayList<ClientContact> getList() {
        ArrayList<ClientContact> list = new ArrayList<>();

        if (clientProfile != null) {
            list.add(new ClientContact("Supervisor", clientProfile.getSupervisorName(), clientProfile.getSupervisorPhone(), null));
            list.add(new ClientContact("Sales", clientProfile.getSalesName(), clientProfile.getSalesPhone(), null));
            list.add(new ClientContact("Designer", clientProfile.getDesignerName(), clientProfile.getDesignerPhone(), null));
        }

        return list;
    }
}