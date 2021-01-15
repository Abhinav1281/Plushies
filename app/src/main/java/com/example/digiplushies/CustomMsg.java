package com.example.digiplushies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomMsg extends AppCompatActivity {
    EditText username,msg;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_msg);
        getSupportActionBar().setTitle("Compile Custom Message");
        username=findViewById(R.id.customUserName);
        msg=findViewById(R.id.customMsg);
        send=findViewById(R.id.customSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseUser> userParseQuery=ParseUser.getQuery();
                userParseQuery.whereEqualTo("username",username.getText().toString());
                List<ParseQuery<ParseUser>> query=new ArrayList<>();
                query.add(userParseQuery);
                ParseQuery<ParseUser> queries=ParseQuery.or(query);
                queries.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(objects.isEmpty())
                        {
                            Toast.makeText(CustomMsg.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                        else
                            sender();
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.mainmenu, popup.getMenu());
        popup.show();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.LogoutUser) {
            ParsePush.unsubscribeInBackground(ParseUser.getCurrentUser().getUsername());
            ParseUser.logOut();
            Intent intent=new Intent(CustomMsg.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.Notification)
        {
            Intent intent=new Intent(CustomMsg.this,Notifications.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    void sender()
    {
        JSONObject data = new JSONObject();
// Put data in the JSON object
        try {
            data.put("title", "Emote Incoming from "+ParseUser.getCurrentUser().getUsername());
            data.put("alert", msg.getText().toString());
        } catch ( JSONException e) {
            // should not happen
            throw new IllegalArgumentException("unexpected parsing error", e);
        }
// Configure the push
        ParsePush push = new ParsePush();
        push.setChannel(username.getText().toString());
        push.setData(data);
        push.sendInBackground();
        ParseObject notiSaver=new ParseObject("Notif");
        notiSaver.put("Sender",ParseUser.getCurrentUser().getUsername());
        notiSaver.put("Message",msg.getText().toString());
        notiSaver.put("Receiver",username.getText().toString());
        String date= new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(new Date());
        notiSaver.put("Creation",date);
        notiSaver.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(CustomMsg.this, "Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(CustomMsg.this, "There was some error\nPlease try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }
}