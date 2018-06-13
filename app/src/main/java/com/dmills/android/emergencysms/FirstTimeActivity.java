package com.dmills.android.emergencysms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class FirstTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);
        setResult(RESULT_OK);

        try{
            // Create the contacts file for later use
            FileOutputStream fos = openFileOutput(ContactsActivity.CONTACTS_FILE, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.close();
        }
        catch(Exception e){
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void end(View view){
        finish();
    }
}
