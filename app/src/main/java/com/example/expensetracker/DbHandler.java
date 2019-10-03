package com.example.expensetracker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class DbHandler extends SQLiteOpenHelper {

    //database
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "expense.db";


    //expense transaction table
    private static final String TABLE_EXPENSE = "expenseDetails";
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATEPICKER = "date_pick";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //onCreate()
    @Override
    public void onCreate(SQLiteDatabase db) {

        // TransactionDetail_tb
        String CREATE_TABLE_EXPENSE = "CREATE TABLE " + TABLE_EXPENSE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_AMOUNT + " REAL,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_DATEPICKER + " REAL"
                + ")";
        db.execSQL(CREATE_TABLE_EXPENSE);
    } //end of onCreate

    /*
    onUpgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);

    }



    /************  transaction event  ************/
    /*
    method to get the transactions from the database
     */
    public ArrayList<HashMap<String, String>> getTrans(){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<HashMap<String, String>> transList = new ArrayList<>();

        String query = "SELECT id, amount, category, date_pick FROM " + TABLE_EXPENSE;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()){
            HashMap<String, String> trans = new HashMap<>();
            trans.put("key_id", cursor.getString(cursor.getColumnIndex(KEY_ID)));
            trans.put("amount", cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
            trans.put("category", cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
            trans.put("date_pick", cursor.getString(cursor.getColumnIndex(KEY_DATEPICKER)));

            transList.add(trans);

        }
        db.close();
        return transList;
    }

    /*
    create a new method to insert transaction details
     */
    public void insertTransDetails(String amount, String category, String date_pick){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cValues = new ContentValues();
        cValues.put(KEY_AMOUNT, amount);
        cValues.put(KEY_CATEGORY, category);
        cValues.put(KEY_DATEPICKER, date_pick);

        long newRodID = db.insert(TABLE_EXPENSE, null, cValues);
        db.close();

    }

    ////create a new method to update transaction details
    public void updateListDetails(int transID,
                                  String amount,
                                  String category,
                                  String date_pick){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_AMOUNT, amount);
        cValues.put(KEY_CATEGORY, category);
        cValues.put(KEY_DATEPICKER, date_pick);
        long newRodID =  db.update(TABLE_EXPENSE, cValues,
                KEY_ID + " =?",
                new String[]{String.valueOf(transID)});
        db.close();

    }

    /*
    adding this method to delete an entry in the database
     */
    public void deleteList(int transID){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EXPENSE,
                KEY_ID + " =?",
                new String[]{String.valueOf(transID)});
        db.close();
    }

}
