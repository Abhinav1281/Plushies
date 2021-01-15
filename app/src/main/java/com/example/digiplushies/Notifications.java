package com.example.digiplushies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Notifications extends AppCompatActivity {
    ListView notifList;
    ArrayAdapter arrayAdapter;
    ArrayList<String> notif=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        notifList=findViewById(R.id.NotifList);

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notif);
        notifList.setAdapter(arrayAdapter);

        ParseQuery notifQuery=ParseQuery.getQuery("Notif");
        notifQuery.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
        notifQuery.orderByDescending("createdAt");
        notifQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for(ParseObject data: objects){
                        String sNotif=data.getString("Sender");
                        sNotif+=":";
                        sNotif+=data.getString("Message");
                        sNotif+=" sent at ";
                        sNotif+=data.get("Creation").toString();
                        notif.add(sNotif);

                    }
                    AfterCreation();
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(Notifications.this, "New Notifications Loaded", Toast.LENGTH_SHORT).show();
                } else {
                    // Something is wrong
                    Toast.makeText(Notifications.this, "SOMETHINIG WENT WRONG\nPLEASE TRY AGAIN LATER", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void AfterCreation()
    {

    }
}