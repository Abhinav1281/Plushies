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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Random;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class DisplayActivity extends AppCompatActivity {
    Button joyBtn,SadBtn,DisgustBtn,AngryBtn,FearBtn;
        ImageView emote;
    EditText recUser;
    int[] joy={R.drawable.joy1,R.drawable.joy2};
    int[] fear={R.drawable.fear1,R.drawable.fear2};
    int[] anger={R.drawable.anger1,R.drawable.anger2,R.drawable.angernew};
    int[] sadness={R.drawable.sadness1,R.drawable.sadness2};
    int[] disgust={R.drawable.disgust1,R.drawable.disgust2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        joyBtn=findViewById(R.id.joyBtn);
        SadBtn=findViewById(R.id.sadBtn);
        DisgustBtn=findViewById(R.id.disgustBtn);
        AngryBtn=findViewById(R.id.angerBtn);
        FearBtn=findViewById(R.id.fearBtn);
        emote=findViewById(R.id.emotionView);
        recUser=findViewById(R.id.senderUserName);
       getSupportActionBar().setTitle("");

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
            Intent intent=new Intent(DisplayActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.Notification)
        {
            Intent intent=new Intent(DisplayActivity.this,Notifications.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void EmotionClick(View view)
    {
        //Toast.makeText(this,recUser.getText().toString(),Toast.LENGTH_SHORT).show();

        if(recUser.getText().toString().equals(""))
            Toast.makeText(this,"USERNAME CANNOT BE EMPTY",Toast.LENGTH_SHORT).show();
        else {
            ParseQuery<ParseUser> userParseQuery=ParseUser.getQuery();
            userParseQuery.whereEqualTo("username",recUser.getText().toString());
            List<ParseQuery<ParseUser>> query=new ArrayList<>();
            query.add(userParseQuery);
            ParseQuery<ParseUser> queries=ParseQuery.or(query);
            queries.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects.isEmpty())
                    {
                        Toast.makeText(DisplayActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Random rnd = new Random();
                        int select;
                        switch (view.getId()) {
                            case R.id.joyBtn:
                                select = rnd.nextInt(2);
                                emote.setImageResource(joy[select]);
                                SenderDialog("JOY");
                                break;

                            case R.id.sadBtn:
                                select = rnd.nextInt(2);
                                emote.setImageResource(sadness[select]);
                                SenderDialog("SAD");
                                break;

                            case R.id.angerBtn:
                                select = rnd.nextInt(3);
                                emote.setImageResource(anger[select]);
                                SenderDialog("ANGER");
                                break;

                            case R.id.disgustBtn:
                                select = rnd.nextInt(2);
                                emote.setImageResource(disgust[select]);
                                SenderDialog("DISGUST");
                                break;

                            case R.id.fearBtn:
                                select = rnd.nextInt(2);
                                emote.setImageResource(fear[select]);
                                SenderDialog("FEAR");
                                break;
                        }
                    }
                }
            });


        }
    }

    void SenderDialog(String emotion)
    {
        PrettyDialog dialog=new PrettyDialog(this);
        dialog.setTitle("Sending Your Emotions ");
        dialog.setCancelable(false);
        dialog.addButton("OK", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });
        switch (emotion)
        {
            case "JOY":
                dialog.setIcon(R.drawable.happyiconimg);
                NotiCompiler("Happy");
                break;

            case "SAD":
                dialog.setIcon(R.drawable.sadiconimg);
                NotiCompiler("Sad");
                break;

            case "FEAR":
                dialog.setIcon(R.drawable.feariconimg);
                NotiCompiler("Scared");
                break;

            case "ANGER":
                dialog.setIcon(R.drawable.angryiconimg);
                NotiCompiler("Angry");
                break;

            case "DISGUST":
                dialog.setIcon(R.drawable.disgusticonimg);
                NotiCompiler("Disgusted");
                break;
        }
        dialog.setCancelable(false);
        dialog.show();

    }


    public void NotiCompiler(String emote)
    {
        dataSaver(emote);
        JSONObject data = new JSONObject();
// Put data in the JSON object
        try {
            data.put("title", "Emote Incoming from "+ParseUser.getCurrentUser().getUsername());
            data.put("alert", "I am very "+emote);
        } catch ( JSONException e) {
            // should not happen
            throw new IllegalArgumentException("unexpected parsing error", e);
        }
// Configure the push
        ParsePush push = new ParsePush();
        push.setChannel(recUser.getText().toString());
        push.setData(data);
        push.sendInBackground();
    }

    public void dataSaver(String Emotion)
    {
        ParseObject notiSaver=new ParseObject("Notif");
        notiSaver.put("Sender",ParseUser.getCurrentUser().getUsername());
        notiSaver.put("Message","I am "+Emotion);
        notiSaver.put("Receiver",recUser.getText().toString());
        String date= new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(new Date());
        notiSaver.put("Creation",date);

        notiSaver.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(DisplayActivity.this,"Sent Successfully",Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(DisplayActivity.this,"There was some error",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void toCustom(View v)
    {
        Intent intent=new Intent(DisplayActivity.this,CustomMsg.class);
        startActivity(intent);
    }
}