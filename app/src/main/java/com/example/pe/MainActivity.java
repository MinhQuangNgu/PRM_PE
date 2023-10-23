package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pe.entity.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Contact> contacts = new ArrayList<Contact>();

        Contact contact = new Contact(1,"Minh","Quang","Quangnm","099999999","sdds");

        contacts.add(contact);

        RecyclerView rec = findViewById(R.id.rec_list);
        rec.setLayoutManager(new LinearLayoutManager(this));
        rec.setAdapter(new ContactCardAdapter(contacts));
    }
}