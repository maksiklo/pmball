package com.pbasket.yellow.basketball;

import android.content.Context;
import android.content.SharedPreferences;

public  class Basketdata {
    private static String duel = "pbasket";
    private SharedPreferences preferences;

    public Basketdata(Context context){
        String NAME = "pbasket";
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void setBasket(String data){
        preferences.edit().putString(Basketdata.duel, data).apply();
    }

    public String getBasket(){
        return preferences.getString(duel, "");
    }
}
