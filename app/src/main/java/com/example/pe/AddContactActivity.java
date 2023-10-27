package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pe.entity.Contact;
import com.example.pe.helpers.FirebaseDatabaseHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    // Declare your UI elements
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText companyEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private Button createButton;

    private Uri selectedImageUri;

    private ImageView imageViewFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Initialize UI elements
        firstNameEditText = findViewById(R.id.txt_profile_firstName);
        lastNameEditText = findViewById(R.id.txt_profile_lastname);
        emailEditText = findViewById(R.id.txt_profile_email);
        companyEditText = findViewById(R.id.txt_profile_company);
        phoneEditText = findViewById(R.id.txt_profile_phone);
        addressEditText = findViewById(R.id.txt_profile_address);
        createButton = findViewById(R.id.button2);
        imageViewFood = findViewById(R.id.profile_img);
        Button addImage = findViewById(R.id.btn_add_img);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text from UI elements
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String company = companyEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String address = addressEditText.getText().toString();

                // Add the contact to the phone book
                addContact(firstName, lastName, email, company, phone, address);
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageViewFood.setImageURI(selectedImageUri);
        }
    }
    private String saveImageToInternalStorage(Uri selectedImageUri) {
        try {
            // Generate a unique file name for the image
            String imageFileName = "food_" + System.currentTimeMillis() + ".png";

            // Open an input stream from the selected image URI
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

            // Create an output stream to the app's internal storage
            FileOutputStream outputStream = openFileOutput(imageFileName, Context.MODE_PRIVATE);

            // Copy the image from the input stream to the output stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();

            // Return the file path to the saved image
            return imageFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addContact(String firstName, String lastName, String email, String company, String phone, String address) {
        int contactId = -1;
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        String imageFileName = null;
        Uri imageUri = Uri.parse("android.resource://" + this.getPackageName() + "/drawable/profilw.jpg");
        if (selectedImageUri == null){
            imageFileName = saveImageToInternalStorage(imageUri);
        } else {
            imageFileName = saveImageToInternalStorage(selectedImageUri);
        }

        // Create a new raw contact
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Add the contact's name
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName)
                .build());

        // Add the contact's email
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .build());

        // Add the contact's organization (company)
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                .build());

        // Add the contact's phone number
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        // Add the contact's address
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, address)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .build());
        try {
            FileInputStream inputStream = openFileInput(imageFileName);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            inputStream.close();

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, imageBytes)
                    .build());

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception here
        }

        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            if (results.length > 0) {
                contactId = (int) ContentUris.parseId(results[0].uri); // Get the contact ID
            }
            //add to firebase
            final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();
            Contact newContact = new Contact(contactId, firstName, lastName, email, address, phone, company, imageFileName);
            dbHelper.addContact(newContact);
            Intent intent =new Intent(AddContactActivity.this, MainActivity.class);
            Toast.makeText(this, "Contact added to phone book", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
        }
    }
}
