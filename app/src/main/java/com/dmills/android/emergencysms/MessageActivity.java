package com.dmills.android.emergencysms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {
    // Key for id of selected message
    final static String MESSAGE_CHECKED = "checkedMessage";
    // Key for custom message String
    final static String CUSTOM_MESSAGE = "customMessage";
    // Key for custom message visibility
    final static String CUSTOM_MESSAGE_VISIBILITY = "customMessageVisible";
    //Key for selected message
    final static String SELECTED_MESSAGE = "selectedMessage";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Retrieve the checked message id and the custom message boolean
        sp = getSharedPreferences(MainActivity.SHARED_PREF_FILENAME, MODE_PRIVATE);
        RadioGroup messageGroup = findViewById(R.id.messageGroup);
        int checkedMessage = sp.getInt(MESSAGE_CHECKED, R.id.rb1);
        boolean customMessageVisible = sp.getBoolean(CUSTOM_MESSAGE_VISIBILITY, false);

        // If a custom message was added, display it
        if(customMessageVisible) {
            RadioButton rb = findViewById(R.id.customMessage);
            rb.setVisibility(View.VISIBLE);
            rb.setText(sp.getString(CUSTOM_MESSAGE, "HELP!"));
        }

        // Set the checked message
        messageGroup.check(checkedMessage);
    }

    // Add the custom message to the RadioGroup and store it in the SharedPreferences
    public void addMessage(View view){
        EditText edit = findViewById(R.id.message);
        String customMessage = edit.getText().toString();
        if(customMessage.equals("")){
            Toast.makeText(this, "Invalid Message", Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(CUSTOM_MESSAGE, customMessage);
            editor.putBoolean(CUSTOM_MESSAGE_VISIBILITY, true);
            editor.apply();

            RadioButton rb = findViewById(R.id.customMessage);
            rb.setText(customMessage);
            rb.setVisibility(View.VISIBLE);
        }
    }

    // Store the id of the selected message and the message itself in the SharedPreferences
    public void setChecked(View view){
        RadioButton selected = findViewById(view.getId());
        String message = selected.getText().toString();

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(MESSAGE_CHECKED, view.getId());
        editor.putString(SELECTED_MESSAGE, message);
        editor.apply();
    }
}
