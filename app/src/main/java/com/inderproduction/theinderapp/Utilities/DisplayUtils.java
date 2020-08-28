package com.inderproduction.theinderapp.Utilities;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class DisplayUtils {

    public static void showToast(Context context,String message, int time){
        Toast.makeText(context,message, time).show();
    }

    public static void disableFields(View... fields){
        for(View v: fields){
            v.setEnabled(false);
            v.setAlpha(0.5f);
        }
    }

    public static void enableFields(View... fields){
        for(View v: fields){
            v.setEnabled(true);
            v.setAlpha(1.0f);
        }
    }

}
