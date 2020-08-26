package com.inderproduction.theinderapp.Utilities;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class CustomUtils {

    public static boolean isInternetConnected(Activity activity){
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
        boolean isDeviceConnected = manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
        return isDeviceConnected;
    }

    public static String getTextFromField(EditText field){
        return field.getText().toString().trim();
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
//            URL url = new URL("https://www.google.com");
//            URLConnection con = url.openConnection();
//            con.setConnectTimeout(5000);
//            if(con.getInputStream().available()>0){
//                return true;
//            } else {
//                return false;
//            }
            Log.e("NETWORK",address + " IS THE ADDRES");
            return !address.equals("");
        } catch (UnknownHostException e) {
            Log.e("NETWORK",e.getMessage());
            return false;
        }
    }

    //CREATE DYNAMIC RETURN TYPE FOR JSON ARRAY
//    public static <T> T[] get(JSONArray jsonArray,Class className) throws JSONException {
//        List<T> list = new ArrayList<>();
//        for(int j = 0;j<jsonArray.length();j++){
//            list.toArray(jsonArray.getClass());
//        }
//         return list.toArray(className);
//    }

    public static String[] createStringArrayFromJSON(JSONArray jsonArray){
        String[] array = new String[jsonArray.length()];
        for(int j = 0;j<jsonArray.length();j++){
            try {
                array[j] = (String) jsonArray.get(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static int[] createIntArrayFromJSON(JSONArray jsonArray){
        int[] array = new int[jsonArray.length()];
        for(int j = 0;j<jsonArray.length();j++){
            try {
                array[j] = (int) jsonArray.get(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

}
