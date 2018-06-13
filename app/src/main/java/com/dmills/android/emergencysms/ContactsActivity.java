package com.dmills.android.emergencysms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    final static String CONTACTS_FILE = "contacts";
    final static String CONTACTS_COUNT = "numContacts";
    private ArrayAdapter<String> contactListAdapter;
    private int numContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Retrieve the number of contacts from SharedPreferences
        SharedPreferences sp = getSharedPreferences(MainActivity.SHARED_PREF_FILENAME, MODE_PRIVATE);
        numContacts = sp.getInt(CONTACTS_COUNT, 0);

        ArrayList<String> contacts = new ArrayList<>();
        contactListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        ListView contactList = findViewById(R.id.contactList);

        try{
            // Open contacts file for reading
            FileInputStream fin = openFileInput(CONTACTS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fin);

            // Retrieve the contacts from the file and populate the list
            for(int i = 0; i < numContacts; i++)
                contactListAdapter.add((String) ois.readObject());

            // Set the listView to display the contacts
            contactList.setAdapter(contactListAdapter);

            ois.close();
            fin.close();
        }
        catch (Exception e){
            Toast.makeText(this, "Error retrieving contact list", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void addContact(View view){
        // Add number to listView
        EditText contact = findViewById(R.id.contactNumber);
        String number = contact.getText().toString();

        if(number.equals("")) {
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
        }
        else {
            contactListAdapter.add(number);
            // Save new number to file
            try {
                FileOutputStream fos = openFileOutput(CONTACTS_FILE, MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                // Write all existing numbers plus the new one to the file
                for (int i = 0; i < contactListAdapter.getCount(); i++)
                    oos.writeObject(contactListAdapter.getItem(i));

                oos.close();
                fos.close();

                // Save the contact counter only if the file was written successfully
                SharedPreferences sp = getSharedPreferences(MainActivity.SHARED_PREF_FILENAME, MODE_PRIVATE);
                numContacts += 1;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(CONTACTS_COUNT, numContacts);
                editor.apply();
            } catch (Exception e) {
                Toast.makeText(this, "Error saving to contact list", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
