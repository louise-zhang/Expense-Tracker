package com.example.expensetracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //date picker
    private TextView dateDisplay;

    //Category Spinner
    private Spinner spinner;
    private ArrayAdapter<String> dataAdapter;

    private Button btnAddTransaction;

    private EditText amount;
    private TextView category;

    private boolean isAdd = true;
    private String addAmount = "0";
    //    private float addAmount;
    private String addCategory;
    private String addDatePick;
    private int id;

    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        findViewById(R.id.btnDatePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        spinner = findViewById(R.id.spCategory);

        amount = findViewById(R.id.etAmount);
        //FLAG DECIMAL
        amount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //set input filter
        amount.setFilters(new InputFilter[]{new InputFilter() {
            @Override

            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                    if (length == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});

        category = findViewById(R.id.txtCategory);
        //for the date picker
        dateDisplay = findViewById(R.id.txtDate);

        //Button Add Transaction
        btnAddTransaction = findViewById(R.id.btnAddTransaction);

        /** https://www.youtube.com/watch?v=FcMiw16bouA  **/
        //Category spinner
        final List<String> categoryList = new ArrayList<>();
        categoryList.add(0, "Select Category");
        categoryList.add("Countdown");
        categoryList.add("Pak'n Save");
        categoryList.add("New World");
        categoryList.add("Kmart");
        categoryList.add("Powershop");
        categoryList.add("AA Insurance");
        categoryList.add("Gym");
        categoryList.add("Petrol");
        categoryList.add("Market");
        categoryList.add("Dress");
        categoryList.add("Rent");

        //style and populate the spinner
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList);

        //dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attach data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //https://stackoverflow.com/questions/9476665/how-to-change-spinner-text-size-and-text-color
                ((TextView) parent.getChildAt(0)).setTextColor(Color.TRANSPARENT);
//                ((TextView) parent.getChildAt(0)).setTextSize(20);

                if (parent.getItemAtPosition(position).equals("Select Category")) {
                    //do nothing
                } else {
                    //on selecting a spinner item
                    String item = parent.getItemAtPosition(position).toString();

                    //show selected spinner item
                    Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();
                    category.setText(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //auto-generated method
            }
        });

        // Add transaction
        Intent addIntent = getIntent();
        isAdd = addIntent.getBooleanExtra(IntentString.IS_ADD, true);
        addAmount = addIntent.getStringExtra(IntentString.AMOUNT);
        addCategory = addIntent.getStringExtra(IntentString.CATEGORY);
        addDatePick = addIntent.getStringExtra(IntentString.DATEPICKER);
        id = addIntent.getIntExtra(IntentString.KEY_ID, -1);

        if (isAdd) {
            btnAddTransaction.setText("ADD");
        } else {
            btnAddTransaction.setText("UPDATE");
            amount.setText(addAmount);
            category.setText(addCategory);
            dateDisplay.setText(addDatePick);
        }

        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uAmount = amount.getText().toString();
                String cate = category.getText().toString();
                String dtPick = dateDisplay.getText().toString();

                boolean isLegal = verifyData(uAmount, cate, dtPick);
                if (!isLegal)
                    return;

                if (isAdd) {
                    add( "$ " + uAmount, cate, dtPick);
                    Toast.makeText(getApplicationContext(), "New transaction added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    update(id, uAmount, cate, dtPick);
                    //intent.setClass(AddExpenseActivity.this, ExpenseDetailActivity.class);
                    Toast.makeText(getApplicationContext(), "Transaction updated", Toast.LENGTH_SHORT).show();
                    finish();
                }

                Intent intent = new Intent();
                intent.setClass(AddExpenseActivity.this, NavActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    } //end of onCreate()

    /*
    Date picker dialog
    */
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int correctMonth = month + 1;
        String date = dayOfMonth + "/" + correctMonth + "/" + year;
        dateDisplay.setText(date);
    }


    /**
     * add method
     *
     * @param uAmount
     * @param aCate
     * @param aDatePick
     */
    private void add(String uAmount, String aCate, String aDatePick) {
        dbHandler = new DbHandler(AddExpenseActivity.this);
        dbHandler.insertTransDetails(uAmount, aCate, aDatePick);
    }

    /**
     * update method
     * @param key
     * @param uAmount
     * @param aCate
     * @param aDatePick
     */
    private void update(int key, String uAmount, String aCate, String aDatePick) {
        dbHandler = new DbHandler(AddExpenseActivity.this);
        dbHandler.updateListDetails(key, uAmount, aCate, aDatePick);
    }


    /**
     * Input cannot be empty
     * @param uAmount
     * @param aCate
     * @param aDatePick
     * @return true : all the params are legal / false :
     */
    private boolean verifyData(String uAmount, String aCate, String aDatePick) {
        if (TextUtils.isEmpty(uAmount)) {
            Toast.makeText(getApplicationContext(), "Amount cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(aCate)) {
            Toast.makeText(getApplicationContext(), "Category cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(aDatePick)) {
            Toast.makeText(getApplicationContext(), "Date cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}