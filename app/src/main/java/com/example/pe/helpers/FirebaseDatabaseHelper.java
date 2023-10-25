package com.example.pe.helpers;

import com.example.pe.entity.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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

    public void addAllContacts(List<Contact> contacts, final OnContactsAddedListener listener) {
        for (Contact contact : contacts) {
            addContact(contact);
        }
        listener.onContactsAdded();
    }

    public interface OnContactsAddedListener {
        void onContactsAdded();
    }

    public void uploadAllContacts(final List<Contact> contacts, final OnContactsUploadedListener listener) {
        deleteAllContacts(new OnContactsDeletedListener() {
            @Override
            public void onContactsDeleted() {
                for (Contact contact : contacts) {
                    addContact(contact);
                }
                listener.onContactsUploaded();
            }

            @Override
            public void onDeleteError(String errorMessage) {
                listener.onUploadError(errorMessage);
            }
        });
    }

    public interface OnContactsUploadedListener {
        void onContactsUploaded();
        void onUploadError(String errorMessage);
    }

    public void deleteAllContacts(final OnContactsDeletedListener listener) {
        databaseReference.child("contacts").removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    listener.onContactsDeleted();
                } else {
                    listener.onDeleteError(databaseError.getMessage());
                }
            }
        });
    }

    public interface OnContactsDeletedListener {
        void onContactsDeleted();
        void onDeleteError(String errorMessage);
    }



}