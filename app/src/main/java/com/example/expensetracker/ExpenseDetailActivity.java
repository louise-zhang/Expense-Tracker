package com.example.expensetracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseDetailActivity extends AppCompatActivity {
    private TextView listAmount;
    private TextView listCategory;
    private TextView listDatePick;

    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        listAmount = findViewById(R.id.listAmount);
        listCategory = findViewById(R.id.listCategory);
        listDatePick = findViewById(R.id.listDatePick);

        updateBtn = (Button) findViewById(R.id.btnEdit);

        Intent intent = getIntent();

        final String amount = intent.getStringExtra(IntentString.AMOUNT);
        listAmount.setText(amount);

        final String category = intent.getStringExtra(IntentString.CATEGORY);
        listCategory.setText(category);

        final String date_pick = intent.getStringExtra(IntentString.DATEPICKER);
        listDatePick.setText(date_pick);

        final int key = intent.getIntExtra(IntentString.KEY_ID, -1);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent detailIntent = new Intent(ExpenseDetailActivity.this, AddExpenseActivity.class);
                detailIntent.putExtra(IntentString.KEY_ID, key);
                detailIntent.putExtra(IntentString.AMOUNT, amount);
                detailIntent.putExtra(IntentString.CATEGORY, category);
                detailIntent.putExtra(IntentString.DATEPICKER, date_pick);
                detailIntent.putExtra(IntentString.IS_ADD, false);

                startActivity(detailIntent);

                Toast.makeText(getApplicationContext(), "Edit transaction", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    } // end of onCreate()

    /*
    Button confirmDelete
     */
    public void confirmDelete(View view) {
        Intent intent = getIntent();
        final int key = intent.getIntExtra(IntentString.KEY_ID, -1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Dialog")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do anything you want
                        Toast.makeText(ExpenseDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        DbHandler db = new DbHandler(ExpenseDetailActivity.this);
                        db.deleteList(key);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ExpenseDetailActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

}
