package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;


import com.example.pe.helpers.ContactsHelper;
import com.example.pe.helpers.FirebaseDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private ContactManager mContactManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();

        dbHelper.clearAllContacts(new FirebaseDatabaseHelper.OnContactsClearedListener() {
            @Override
            public void onContactsCleared() {
                ContactsHelper.importContactsToFirebase(dbHelper, getContentResolver());
            }

            @Override
            public void onContactsClearError(String errorMessage) {
            }
        });
      
        mContactManager = new ContactManager(this);
        List<Contact> contacts = new ArrayList<Contact>();
        contacts = mContactManager.getListContact();
        Contact contact = new Contact(55,"Minh","Quang","Quangnm","099999999","sdds");
        Contact contact2 = new Contact(56,"Minh2","Quang2","Quangnm2","099999999","sdds");
        contacts.add(contact);
        contacts.add(contact2);
//        contacts.addAll(mContactManager.getListContact());
        for (Contact c:contacts) {
            Log.d("contactCheck", "onCreate: "+c.toString());
        }
        RecyclerView rec = findViewById(R.id.rec_list);
        rec.setLayoutManager(new LinearLayoutManager(this));
        rec.setAdapter(new ContactCardAdapter(contacts));
    }

}