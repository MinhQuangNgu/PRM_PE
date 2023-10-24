package com.example.pe.helpers;

import androidx.annotation.NonNull;
import com.example.pe.entity.Contact;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseHelper {
    private DatabaseReference databaseReference;

    public FirebaseDatabaseHelper() {
        // Connect Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addContact(Contact contact) {
        String contactId = String.valueOf(contact.id);
        databaseReference.child("contacts").child(contactId).setValue(contact);
    }

    public void updateContact(Contact contact) {
        String contactId = String.valueOf(contact.id);
        databaseReference.child("contacts").child(contactId).setValue(contact);
    }

    public void deleteContact(int contactId) {
        databaseReference.child("contacts").child(String.valueOf(contactId)).removeValue();
    }

    public void getContact(int contactId, final OnContactFetchedListener listener) {
        String contactIdString = String.valueOf(contactId);
        DatabaseReference contactRef = databaseReference.child("contacts").child(contactIdString);
        contactRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                if (contact != null) {
                    listener.onContactFetched(contact);
                } else {
                    listener.onContactNotFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onContactFetchError(databaseError.getMessage());
            }
        });
    }

    public interface OnContactFetchedListener {
        void onContactFetched(Contact contact);
        void onContactNotFound();
        void onContactFetchError(String errorMessage);
    }

    public void clearAllContacts(final OnContactsClearedListener listener) {
        DatabaseReference contactsRef = databaseReference.child("contacts");
        contactsRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onContactsCleared();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onContactsClearError(e.getMessage());
                    }
                });
    }

    public interface OnContactsClearedListener {
        void onContactsCleared();
        void onContactsClearError(String errorMessage);
    }
}
