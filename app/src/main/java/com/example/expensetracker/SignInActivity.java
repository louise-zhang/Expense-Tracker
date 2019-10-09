package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    //Existence time, initial value is 0, used to calculate subtraction with the current time (milliseconds)
    private long mExitTime;

    private Button btnSignUp;
    private Button btnLogin;

    private EditText editUser;
    private EditText editPwd;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        editUser = (EditText) findViewById(R.id.etUsername);
        editPwd = (EditText) findViewById(R.id.etPassword);

        rememberPass = (CheckBox) findViewById(R.id.remember_pwd);


        /*
        Remember password
        */
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            // get the account and password to editView
            String username = pref.getString("username", "");
            String password = pref.getString("password", "");
            editUser.setText(username);
            editPwd.setText(password);
            rememberPass.setChecked(true);
        }

        /*
       login event
        */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginCheck.isEmpty(editUser.getText().toString()) || LoginCheck.isEmpty(editPwd.getText().toString())) {
                    Toast.makeText(SignInActivity.this, "Please enter a valid email or password", Toast.LENGTH_SHORT).show();
                    return;
                }


                //save login account info to SharedPreferences
                SharedPreferences mySharedPreferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();//SharedPreferences.Editor
                editor.putString("accountDetails", editUser.getText().toString()); //putString method to save info
                editor.commit(); //

                String username = editUser.getText().toString();
                String password = editPwd.getText().toString();
                // if username is admin@xyz.com and password is 123456，login successfully
                if (username.equals("admin@xyz.com") && password.equals("123456")) {
                    editor = pref.edit();
                    if (rememberPass.isChecked()) { // check if checkBox is ticked
                        editor.putBoolean("remember_password", true);
                        editor.putString("username", username);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent loginIntent = new Intent(SignInActivity.this, NavActivity.class);
                    startActivity(loginIntent);
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "Account or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        }); // end of login event


         /*
         sign up event
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

    } // end of onCreate()

    /*
    Press back twice to exit app
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                //System.currentTimeMillis()system current time
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
