package com.dmills.android.emergencysms;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class EmergencyWidget extends AppWidgetProvider {
    final static String CUSTOM_ACTION = "SEND_EMERGENCY_SMS";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.emergency_widget);

            Intent i = new Intent(context, getClass());
            i.setAction(com.dmills.android.emergencysms.EmergencyWidget.CUSTOM_ACTION);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
            views.setOnClickPendingIntent(R.id.emergencyButton, pi);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(CUSTOM_ACTION.equals(intent.getAction())){

            SmsManager sms = SmsManager.getDefault();
            SharedPreferences sp = context.getSharedPreferences(MainActivity.SHARED_PREF_FILENAME, Context.MODE_PRIVATE);
            ArrayList<String> contacts = new ArrayList<>();

            String message = sp.getString(MessageActivity.SELECTED_MESSAGE, "HELP!");
            int numContacts = sp.getInt(ContactsActivity.CONTACTS_COUNT, 1);

            try{
                // Read contact list from file and store in list
                FileInputStream fis = context.openFileInput(ContactsActivity.CONTACTS_FILE);
                ObjectInputStream ois = new ObjectInputStream(fis);

                for(int i = 0; i < numContacts; i++)
                    contacts.add((String) ois.readObject());

                ois.close();
                fis.close();

            }
            catch (Exception e){
                Toast.makeText(context, "Failed to read file", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            // Send message to each contact
            try{
                for(String number: contacts)
                    sms.sendTextMessage(number, null, message, null, null);
                Toast.makeText(context, "Messages sent", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(context, "Message failed to send", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }
}

