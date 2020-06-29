package com.example.mininotes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("filename", context.MODE_PRIVATE);

    }
    public void setState(Boolean state)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("Nightmode",state);
        editor.commit();
    }
    public  boolean loadState()
    {
        Boolean state=sharedPreferences.getBoolean("Nightmode",false);
        return state;

    }
}
