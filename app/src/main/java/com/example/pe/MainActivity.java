package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pe.helpers.ContactsHelper;
import com.example.pe.helpers.FirebaseDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseDatabaseHelper dbHelper = new FirebaseDatabaseHelper();


        dbHelper.clearAllContacts(new FirebaseDatabaseHelper.OnContactsClearedListener() {
            @Override
            public void onContactsCleared() {
                ContactsHelper.importContactsToFirebase(dbHelper, getContentResolver());
            }

            @Override
            public void onContactsClearError(String errorMessage) {
            }
        });
    }

}