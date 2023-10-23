package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pe.entity.Contact;
import com.example.pe.helpers.FirebaseDatabaseHelper;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
        Contact newContact = new Contact(1, "Cong", "Nguyen", "nguyencong@gmail.com", "FPT", "0123456789", "FPTU", "https://i.pinimg.com/474x/f1/8a/e9/f18ae9cf47240876a977e6071db7f1f2.jpg");
        dbHelper.addContact(newContact);
    }
}