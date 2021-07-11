package com.example.smartpad.utils;

import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static String getTrimmedString(EditText editText){
        return editText.getText().toString().trim();
    }

    public static long getDateMillisFromCurrentMillis(long millis){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        String date=day+"/"+month+"/"+year;
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dat=dateFormat.parse(date);
            long newTime=dat.getTime();
            return newTime;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static String getHexFromRGB(int r, int g, int b){
        String hex = String.format("#%02x%02x%02x", r, g, b);
        return hex;
    }
}
