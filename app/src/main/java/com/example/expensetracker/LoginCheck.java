package com.example.expensetracker;

import android.text.format.Time;
import android.view.KeyEvent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginCheck {
    /**
     * check string is empty
     * @param input
     * @return
     */
    public static boolean isEmpty( String input )
    {
        if ( input == null || "".equals( input ) )
            return true;

        for ( int i = 0; i < input.length(); i++ )
        {
            char c = input.charAt( i );
            if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
            {
                return false;
            }
        }
        return true;
    }

    //https://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        //Acceptable email formats include:
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        CharSequence inputStr = email;
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}

