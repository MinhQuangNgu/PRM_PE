package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.pe.entity.Contact;
import com.example.pe.helpers.FirebaseDatabaseHelper;

import java.util.Objects;

public class ContactDetailActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText companyEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private Button createButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        firstNameEditText = findViewById(R.id.txt_profile_firstName);
        lastNameEditText = findViewById(R.id.txt_profile_lastname);
        emailEditText = findViewById(R.id.txt_profile_email);
        companyEditText = findViewById(R.id.txt_profile_company);
        phoneEditText = findViewById(R.id.txt_profile_phone);
        addressEditText = findViewById(R.id.txt_profile_address);
        createButton = findViewById(R.id.button2);

        String id = getIntent().getStringExtra("CONTACT_ID");
        if(id==null || id.isEmpty()){
            Log.d("Get_data_by_id", "Unavailable id");
            Intent intent = new Intent(ContactDetailActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
        int contactID;
        try{
            contactID = Integer.parseInt(id);
        }catch (Exception e){
            Log.d("Get_data_by_id", Objects.requireNonNull(e.getMessage()));
            Intent intent = new Intent(ContactDetailActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        dbHelper.getContactById(contactID, new FirebaseDatabaseHelper.OnContactByIdFetchedListener() { // gọi hàm get contact by id trên firebase
            @Override
            public void onContactByIdFetched(Contact contact) {
                firstNameEditText.setText(contact.getFirstName());
                lastNameEditText.setText(contact.getLastName());
                emailEditText.setText(contact.getEmail());
                companyEditText.setText(contact.getCompany());
                phoneEditText.setText(contact.getPhone());
                addressEditText.setText(contact.getAddress());
                Log.d("Contact", contact.toString());
            }

            @Override
            public void onContactByIdFetchError(String errorMessage) {
                // ở đây sẽ xử lý nêu lỗi, không cần làm gì cx đc
                Log.e("ContactFetchError", errorMessage);
            }
        });
        //Contact contact = dbHelper.getContact(id, null);
    }
}