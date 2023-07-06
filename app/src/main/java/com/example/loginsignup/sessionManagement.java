package com.example.loginsignup;


import android.content.Context;
import android.content.SharedPreferences;

public class sessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public sessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveSession(CurrentUser userObj){
        int id = userObj.getId();
        editor.putInt(SESSION_KEY,id).commit();


    }
    public int getSession()
    {
        return  sharedPreferences.getInt(SESSION_KEY,-1);
    }
    public void removeSession(){
        editor.putInt(SESSION_KEY,-1).commit();
    }

}

