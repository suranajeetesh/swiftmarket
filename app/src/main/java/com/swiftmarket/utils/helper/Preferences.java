package com.swiftmarket.utils.helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import com.swiftmarket.R;

public class Preferences {

    public static String PREF_NAME = "";

    public static final int MODE = Context.MODE_PRIVATE;
    public static SharedPreferences sharedPreferences;
    public static Preferences preferences;
    public static SharedPreferences.Editor editor;



    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.app_name), MODE);
    }
    public static void clearPreference(Context context) {
        SharedPreferences.Editor editor =getPreferences(context).edit();
        editor.clear();
        editor.apply();
    }


    public Preferences(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
        editor=getPreferences(context).edit();
    }


    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

}