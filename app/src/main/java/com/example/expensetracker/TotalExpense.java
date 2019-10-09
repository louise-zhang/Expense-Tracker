package com.example.expensetracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenuListView;

public class TotalExpense extends AppCompatActivity {

    private TextView num1Amount;

    private double num, num2, sum, difference, product, quotient;

//    SwipeMenuListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        num1Amount = findViewById(R.id.tvAmount);

        DashboardFragment.listView = findViewById(R.id.trans_list);
    }

    //constructor
    public TotalExpense(double num1, double num2) {

        this.num = Integer.parseInt(num1Amount.getText().toString());
        this.num2 = num2;
    }

    //getter
    public double getNum1() {
        return num;
    }

    public double getNum2() {
        return num2;
    }

//    public double add() {
//        if (DashboardFragment.listView.getItemAtPosition(0).equals("Countdown")) {
//            sum = getNum1() + getNum2();
//            return sum;
//        }
//    }


    public double subtract() {
        if (getNum1() > getNum2()) {
            difference = getNum1() - getNum2();
        } else {
            difference = getNum2() - getNum1();
        }
        return difference;
    }

    public double multiply() {
        product = getNum1() * getNum2();
        return product;
    }

    public double divide() {
        if (getNum1() > getNum2()) {
            quotient = getNum1() / getNum2();
        } else {
            difference = getNum2() / getNum1();
        }
        return quotient;
    }


}
