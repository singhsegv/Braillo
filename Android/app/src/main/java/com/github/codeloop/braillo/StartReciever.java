package com.github.codeloop.braillo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by dilpreet on 7/5/17.
 */

public class StartReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int counter=0;
        if(PreferenceManager.getDefaultSharedPreferences(context).contains("counter"))
            counter = PreferenceManager.getDefaultSharedPreferences(context).getInt("counter",0);
        if(counter%2==0)
            context.startActivity(new Intent(context,MainActivity.class));
        else
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("counter",counter+1).apply();

        Log.d("mytag",counter+" counter");
    }
}
