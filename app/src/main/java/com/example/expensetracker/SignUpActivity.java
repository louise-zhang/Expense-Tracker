package com.example.expensetracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSignIn;
    private Button btnCreateAccount;

    private EditText editUser;
    private EditText editPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editUser = (EditText) findViewById(R.id.etUsername);
        editPwd = (EditText) findViewById(R.id.etPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnSignIn = findViewById(R.id.btnLogin);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bckToLoginIntent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(bckToLoginIntent);
            }
        });


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isTrue = true;
                if (LoginCheck.isEmailValid(editUser.getText().toString()) == false) {
                    isTrue = false;
                    Toast.makeText(SignUpActivity.this, "Incorrect email address format!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (LoginCheck.isEmpty(editPwd.getText().toString())) {
                    isTrue = false;
                    Toast.makeText(SignUpActivity.this, "Password can't be blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isTrue = true) {
                    //call DBOpenHelper
                    DbHandler handler = new DbHandler(SignUpActivity.this);
                    SQLiteDatabase db = handler.getWritableDatabase();
                    Cursor c = db.query("accountDetails", null,
                            "username=? and password=?",
                            new String[]{editUser.getText().toString(), editPwd.getText().toString()},
                            null, null, null);
                    if (c != null && c.getCount() >= 1) {
                        Toast.makeText(SignUpActivity.this,
                                "Username already exists",
                                Toast.LENGTH_SHORT).show();
                        c.close();
                    } else {
                        //insert data
                        ContentValues values = new ContentValues();
                        values.put("username", editUser.getText().toString());
                        values.put("password", editPwd.getText().toString());
                        long rowid = db.insert("accountDetails", null, values);

                        Toast.makeText(SignUpActivity.this, "You have successfully registered and please sign in.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    db.close();
                } else {
                    return;
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }
}
