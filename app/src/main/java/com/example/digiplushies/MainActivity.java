package com.example.digiplushies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button signUp,toLogin;
    EditText userName,pswd,email;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        signUp=findViewById(R.id.signINBtn);
        userName=findViewById(R.id.SignUpUserName);
        pswd=findViewById(R.id.SignUpPassword);
        toLogin=findViewById(R.id.toLogin);
        email=findViewById(R.id.emailAddress);
        getSupportActionBar().hide();
        //FOR NOTIFICATIONS


        if(ParseUser.getCurrentUser()!=null)
        {
            ToLogin();
        }
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSignIn();
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToLogin();
            }
        });


    }

    void ToLogin()
    {
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void UserSignIn()
    {
        AlertDialog.Builder Loginalert=new AlertDialog.Builder(MainActivity.this);
        Loginalert.setTitle("Signing Up!");
        Loginalert.setMessage("Please Wait!!");
        Loginalert.setCancelable(false);
        AlertDialog SignUpAlert=Loginalert.show();
        ParseUser user = new ParseUser();
// Set the user's username and password, which can be obtained by a forms
        user.setUsername(userName.getText().toString());
        user.setPassword(pswd.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                SignUpAlert.dismiss();
                if (e == null) {
                    AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Sign in Successful!")
                            .setMessage("Welcome "+userName.getText().toString()+"!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ParsePush.subscribeInBackground(userName.getText().toString());
                                    finish();
                                    ToLogin();
                                }
                            });
                    AlertDialog LinAlert=alert.show();
                } else {
                    ParseUser.logOut();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}