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

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });
    }
}