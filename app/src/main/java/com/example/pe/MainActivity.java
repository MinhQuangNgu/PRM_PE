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


        // Example: demo all fetch data

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
        dbHelper.getContactsBySearch(searchText, new FirebaseDatabaseHelper.OnContactsBySearchListener() { // gọi hàm get contact by name,email,.. trên firebase
            @Override
            public void onContactsBySearchFetched(List<Contact> contacts) { // xử lý danh sách contacts lấy được
                // ở đây sẽ trả về List<Contact> contacts, xử lý vs danh sách contacts trả về như xử lý bình thường...
                //hiển thị trên màn hình,.... các thứ ở đây
                for (Contact contact : contacts) {
                    Log.d("Contact", contact.toString());
                }
            }

            @Override
            public void onContactsBySearchFetchError(String errorMessage) {
                // ở đây sẽ xử lý nêu lỗi, không cần làm gì cx đc
                Log.e("ContactFetchError", errorMessage);
            }
        });

        // tương tự với getbyid

        int contactId = 100;
        dbHelper.getContactById(contactId, new FirebaseDatabaseHelper.OnContactByIdFetchedListener() { // gọi hàm get contact by id trên firebase
            @Override
            public void onContactByIdFetched(Contact contact) { // xử lý contact lấy được
                //hiển thị trên màn hình,.... các thứ ở đây
                Log.d("Contact", contact.toString());
            }

            @Override
            public void onContactByIdFetchError(String errorMessage) {
                // ở đây sẽ xử lý nêu lỗi, không cần làm gì cx đc
                Log.e("ContactFetchError", errorMessage);
            }
        });

        //addContact

        Contact newContact = new Contact(103, "Nguyen", "Cong", "nguyen@gmail.com", "FPT", "0123456789", "FPT", "avata.jpg");
        dbHelper.addContact(newContact);

        //updateContact
        newContact.firstName = "Hello";
        dbHelper.updateContact(newContact);

        //deleteContact
        //int contactDelId = 101;
        //dbHelper.deleteContact(contactDelId);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });

    }
}