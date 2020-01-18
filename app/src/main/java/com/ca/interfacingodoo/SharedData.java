package com.ca.interfacingodoo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedData {
    public static final String MY_PREFERENCES = "myPrefs";
    private static SharedPreferences sharedPreferences;

    public static String getKey(Context c, String key){
        sharedPreferences = c.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public static void setKey(Context c, String key, String value){
        sharedPreferences = c.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

}
