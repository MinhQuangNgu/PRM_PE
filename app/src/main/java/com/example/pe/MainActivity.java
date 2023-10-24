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

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
        Contact newContact = new Contact(1, "Cong", "Nguyen", "nguyencong@gmail.com", "FPT", "0123456789", "FPTU", "https://i.pinimg.com/474x/f1/8a/e9/f18ae9cf47240876a977e6071db7f1f2.jpg");
        dbHelper.addContact(newContact);

    }
}