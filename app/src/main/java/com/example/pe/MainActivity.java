package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pe.entity.Contact;
import com.example.pe.helpers.FirebaseDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ContactManager mContactManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContactManager = new ContactManager(this);
        List<Contact> contacts = mContactManager.getListContact();
        for (Contact c:contacts) {
            Log.d("contactCheck", "onCreate: "+c.toString());
        }
        RecyclerView rec = findViewById(R.id.rec_list);
        rec.setLayoutManager(new LinearLayoutManager(this));
        rec.setAdapter(new ContactCardAdapter(contacts));

        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
        dbHelper.uploadAllContacts(contacts, new FirebaseDatabaseHelper.OnContactsUploadedListener() {
            @Override
            public void onContactsUploaded() {
                Log.d("FirebaseLoad", "Contacts load successfully");
            }
            @Override
            public void onUploadError(String errorMessage) {
                Log.e("FirebaseLoadError", errorMessage);
            }
        });


        // Example

        // Add contacts
        List<Contact> newContacts = new ArrayList<>();
        newContacts.add(new Contact(100, "Nguyen", "Cong", "nguyen@gmail.com", "FPT", "0123456789", "FPT", "avata.jpg"));
        newContacts.add(new Contact(101, "Cong", "Nguyen", "cong@gmail.com", "HN", "0123456789", "FPT", "avata.jpg"));
        dbHelper.addAllContacts(newContacts, new FirebaseDatabaseHelper.OnContactsAddedListener() {
            @Override
            public void onContactsAdded() {
                Log.d("Contact", "Added.");
            }
        });

        String searchText = "Cong";
        dbHelper.getContactsBySearch(searchText, new FirebaseDatabaseHelper.OnContactsFetchedListener() {
            @Override
            public void onContactsFetched(List<Contact> contacts) {
                for (Contact contact : contacts) {
                    Log.d("Contact", contact.toString());
                }
            }

            @Override
            public void onContactsFetchError(String errorMessage) {
                Log.e("ContactFetchError", errorMessage);
            }
        });



        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });

    }
}