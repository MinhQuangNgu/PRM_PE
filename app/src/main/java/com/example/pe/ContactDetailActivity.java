package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        Button updateButton = findViewById(R.id.button2);

        String id = null;
        Intent intentCurrent = getIntent();
        if (intentCurrent != null && intentCurrent.hasExtra("CONTACT_ID")) {
            id = intentCurrent.getStringExtra("CONTACT_ID");
        }
        if (id == null || id.isEmpty()) {
            Intent intent = new Intent(ContactDetailActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
        int contactID;
        try {
            contactID = Integer.parseInt(id);
        } catch (Exception e) {
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
                Log.e("ContactFetchError", errorMessage);
                Intent intent = new Intent(ContactDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        updateButton.setOnClickListener(v -> {
            Log.d("Get_data_by_id", "Check on click ");
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String company = companyEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String address = addressEditText.getText().toString();

            // Add the contact to the phone book
            addContact(contactID, firstName, lastName, email, company, phone, address);
        });
    }

    private void addContact(int contactId, String firstName, String lastName, String email, String company, String phone, String address) {
        ContentResolver contentResolver = getContentResolver();

        //add to firebase
        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
        Contact newContact = new Contact(contactId, firstName, lastName, email, address, phone, company, "https://i.pinimg.com/474x/f1/8a/e9/f18ae9cf47240876a977e6071db7f1f2.jpg");
        dbHelper.updateContact(newContact);
        Toast.makeText(this, "Contact added to phone book", Toast.LENGTH_SHORT).show();

        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Cursor cursor = contentResolver.query(contactUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName);
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName);
            contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, email);
            contentValues.put(ContactsContract.CommonDataKinds.Organization.COMPANY, company);
            contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            contentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, address);

            int rowsUpdated = contentResolver.update(
                    contactUri,
                    contentValues,
                    null,
                    null
            );

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Contact added to phone book", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("ContactUpdater", "Update fail");
                Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
            }
        }
    }
}