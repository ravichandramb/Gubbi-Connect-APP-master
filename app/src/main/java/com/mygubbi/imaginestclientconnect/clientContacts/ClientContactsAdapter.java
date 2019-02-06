package com.mygubbi.imaginestclientconnect.clientContacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygubbi.imaginestclientconnect.R;

import java.util.ArrayList;

/**
 * @author Hemanth
 * @since 4/18/2018
 */
public class ClientContactsAdapter extends RecyclerView.Adapter<ClientContactsAdapter.ContactsViewHolder> {

    private ArrayList<ClientContact> contactsList;

    ClientContactsAdapter(ArrayList<ClientContact> contactsList) {
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();

        ClientContact contact = contactsList.get(position);

        holder.textTitle.setText(contact.getTitle());
        String text = contact.getName() + " - " + contact.getPhone();
        holder.textName.setText(text);

        if (contact.getTitle().equalsIgnoreCase("Supervisor")) {
            holder.imageContactPhoto.setImageResource(R.drawable.ic_supervisor);
        } else if (contact.getTitle().equalsIgnoreCase("Sales")) {
            holder.imageContactPhoto.setImageResource(R.drawable.ic_sales);
        } else if (contact.getTitle().equalsIgnoreCase("Designer")) {
            holder.imageContactPhoto.setImageResource(R.drawable.ic_designer);
        } else {
            holder.imageContactPhoto.setImageResource(R.drawable.ic_builder);
        }
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageContactPhoto;
        private TextView textTitle, textName;

        ContactsViewHolder(View itemView) {
            super(itemView);

            imageContactPhoto = itemView.findViewById(R.id.image_contact_photo);
            textTitle = itemView.findViewById(R.id.text_title);
            textName = itemView.findViewById(R.id.text_name);
        }
    }
}
