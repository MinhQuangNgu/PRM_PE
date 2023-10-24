package com.example.pe.helpers;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.net.Uri;

import com.example.pe.entity.Contact;

public class ContactsHelper {
    public static void importContactsToFirebase(FirebaseDatabaseHelper dbHelper, ContentResolver contentResolver) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = 0;
            do {
                @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String email = "";
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                @SuppressLint("Range") String address = "";
                @SuppressLint("Range") String company = "";
                @SuppressLint("Range") String imageUri = "";

                // Create a Contact object and add it to Firebase
                Contact contact = new Contact(id, firstName, lastName, email, address, phone, company, imageUri);
                dbHelper.addContact(contact);
                id++;
            } while (cursor.moveToNext());

            cursor.close();
        }
    }
}
