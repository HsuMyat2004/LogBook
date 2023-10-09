package com.kmd.uog.logbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmd.uog.logbook.adapter.ContactAdapter;
import com.kmd.uog.logbook.database.Contact;
import com.kmd.uog.logbook.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {
    private List<Contact> contactList = new ArrayList<>();
    private DatabaseHelper dataBaseHelper;
    private ContactAdapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_database_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataBaseHelper = new DatabaseHelper(getBaseContext());
        contactAdapter = new ContactAdapter(contactList);
        contactAdapter.setListener(new ContactAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, long id) {
                if(id == R.id.btnRemove)
                {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Contact contact =contactList.get(position);
                                dataBaseHelper.delete(contact.getId());
                                contactList = dataBaseHelper.search("");
                                contactAdapter.setContactList(contactList);
                                contactAdapter.notifyDataSetChanged();

                            } catch (Exception e) {

                            }

                        }
                    });
                }else if (id==R.id.btnEdit){
                    Contact contact =contactList.get(position);
                    Intent intent =new Intent(getBaseContext(), ContactEntryActivity.class);
                    intent.putExtra(DatabaseHelper.CONTACT_ID, contact.getId());

                    intent.putExtra(DatabaseHelper.NAME, contact.getName());
                    intent.putExtra(DatabaseHelper.DATE, contact.getDate());
                    intent.putExtra(DatabaseHelper.EMAIL, contact.getEmail());
                    startActivityForResult(intent,UPDATE_RESULT);
                }
            }
        });
        recyclerView.setAdapter(contactAdapter);

        //data read
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    contactList = dataBaseHelper.search("");
                    contactAdapter.setContactList(contactList);
                    contactAdapter.notifyDataSetChanged(); // refresh the data

                } catch (Exception e) {

                }

            }
        });

    }
    public static final int UPDATE_RESULT= 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == UPDATE_RESULT && resultCode== RESULT_OK )
        {
            runOnUiThread(new Runnable() {
                @Override
            public void run() {
            try {
                contactList = dataBaseHelper.search("");
                contactAdapter.setContactList(contactList);
                contactAdapter.notifyDataSetChanged(); // refresh the data

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
