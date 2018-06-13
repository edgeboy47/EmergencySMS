package com.dmills.android.emergencysms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    final static int FIRST_TIME_REQUEST = 0;
    final static String FIRST_TIME_FLAG = "com.dmills.emergencysms.FIRST_TIME";
    final static String SHARED_PREF_FILENAME = "com.dmills.emergencysms.SHARED_PREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Used to show the Intro screen only when the app is opened for the first time
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_FILENAME, MODE_PRIVATE);
        if(!sp.getBoolean(FIRST_TIME_FLAG, false)){
            Intent i = new Intent(this, FirstTimeActivity.class);
            startActivityForResult(i, FIRST_TIME_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Set the first time flag to true after the intro screen is finished
        if(requestCode == FIRST_TIME_REQUEST && resultCode == RESULT_OK){
            SharedPreferences sp = getSharedPreferences(SHARED_PREF_FILENAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean(FIRST_TIME_FLAG, true);
            edit.apply();
        }
    }

    public void onClick(View view){
        Intent i = null;

        switch (view.getId()) {
            case R.id.contactsButton:
                i = new Intent(this, ContactsActivity.class);
                break;

            case R.id.messagesButton:
                i = new Intent(this, MessageActivity.class);
                break;
        }
        startActivity(i);
    }
}
