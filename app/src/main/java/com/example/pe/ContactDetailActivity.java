package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pe.entity.Contact;
import com.example.pe.helpers.FirebaseDatabaseHelper;

public class ContactDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        String id = getIntent().getStringExtra("CONTACT_ID");
        if(id==null || id.isEmpty()){
            Intent intent = new Intent(ContactDetailActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
        //Contact contact = dbHelper.getContact(id, null);
    }
}