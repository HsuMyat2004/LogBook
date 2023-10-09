package com.kmd.uog.logbook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmd.uog.logbook.R;
import com.kmd.uog.logbook.database.Contact;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> contactList;
    private ClickListener listener;

    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.lblName.setText(contact.getName());
        holder.lblDate.setText(contact.getDate());
        holder.lblEmail.setText(contact.getEmail());

        // Load and display the image using Picasso
        if (contact.getImageFilePath() != null) {
            Picasso.get()
                    .load("file://" + contact.getImageFilePath()) // Load from file path
                    .into(holder.imgContact);
        } else {
            // Handle the case where there is no image file path
            // You can set a default image or hide the ImageView
            holder.imgContact.setImageResource(R.drawable.user);
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v, long id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblName, lblDate, lblEmail;
        Button btnRemove, btnEdit;
        ImageView imgContact; // ImageView for displaying contact images

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblName = itemView.findViewById(R.id.lblName);
            lblDate = itemView.findViewById(R.id.lblDate);
            lblEmail = itemView.findViewById(R.id.lblEmail);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            imgContact = itemView.findViewById(R.id.userImage); // Initialize the ImageView

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition(), view, R.id.btnRemove);
                    }
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition(), view, R.id.btnEdit);
                    }
                }
            });
        }
    }
}
