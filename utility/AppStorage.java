package com.gaurabneupane.id424.utility;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class AppStorage {
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_TOKEN = "token";
    SharedPreferences sharedPreferences;
    public AppStorage(Context context){
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    public void saveToken(String token){
        if(token == null ){
            sharedPreferences.edit().remove(KEY_TOKEN).apply();
        }
        else{
            sharedPreferences.edit().putString(KEY_TOKEN, token).apply();
        }
    }

    public String getToken(){
        return sharedPreferences.getString(KEY_TOKEN, null);
    }
}
