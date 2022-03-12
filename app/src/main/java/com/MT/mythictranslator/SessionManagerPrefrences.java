package com.MT.mythictranslator;

import android.content.Context;
import android.content.SharedPreferences;

class SessionManagerPrefrences {
    SharedPreferences prefs = null;

    private Context gContext = null;

    private SharedPreferences.Editor editor = null;

    private final String PREF_NAME = "PassVault";

    private final int PREF_MODE = 0;

    SessionManagerPrefrences(Context context){
        if (context != null) {
            gContext = context;
            prefs = context.getSharedPreferences(PREF_NAME, PREF_MODE);
            editor = prefs.edit();
        }
    }

    public final String KEY_NAME = "current_font";

    public void setKeyUserName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public String  getKeyUserName(){
        return prefs.getString(KEY_NAME, null);
    }
}