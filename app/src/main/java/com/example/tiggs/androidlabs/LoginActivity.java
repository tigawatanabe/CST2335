package com.example.tiggs.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = "LoginActivity";

    public static final String MyPreference = "myPref";

    SharedPreferences sharedPreferences;
    Button loginButton;
    EditText email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        //reference EditText
        email=findViewById(R.id.editOne);
        password=findViewById(R.id.editTwo);

        //reference the button from XML
        loginButton=findViewById(R.id.loginbutton);

        //make sharedPreferences
        sharedPreferences = getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
        //set email EditText text
        email.setText(sharedPreferences.getString("DefaultEmail", "email.domain.com"));

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String e = email.getText().toString();
                String p = password.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("DefaultEmail",e);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
