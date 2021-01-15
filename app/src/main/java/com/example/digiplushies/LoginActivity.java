package com.example.digiplushies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    Button Login,toSignUp;
    EditText userName,pswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        if(ParseUser.getCurrentUser()!=null)
        {
            ToDisplay();
        }

        Login=findViewById(R.id.LoginBtn);
        userName=findViewById(R.id.LoginUserName);
        pswd=findViewById(R.id.LoginPassword);
        toSignUp=findViewById(R.id.toSignUp);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });
        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToSignUp();
            }
        });
    }

    void ToSignUp()
    {
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void ToDisplay()
    {
        Intent intent=new Intent(LoginActivity.this,DisplayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void UserLogin()
    {
        //Toast.makeText(this,"Inside User Login",Toast.LENGTH_SHORT).show();
        AlertDialog.Builder Loginalert=new AlertDialog.Builder(LoginActivity.this);
        Loginalert.setTitle("Signing In!");
        Loginalert.setMessage("Please Wait!!");
        AlertDialog SignUpAlert=Loginalert.show();
        ParseUser.logInInBackground(userName.getText().toString(),pswd.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser parseUser, ParseException e) {
            SignUpAlert.dismiss();
            if (parseUser != null) {
                AlertDialog.Builder alert=new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("Sign in Successful!")
                        .setMessage("Welcome "+userName.getText().toString()+"!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParsePush.subscribeInBackground(userName.getText().toString());
                                finish();
                                ToDisplay();
                            }
                        });
                AlertDialog LinAlert=alert.show();
            } else {
                ParseUser.logOut();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    });
    }
}