package com.github.codeloop.braillo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by dilpreet on 7/5/17.
 */

public class StartService extends Service {


    ContentObserver mSettingsContentObserver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mSettingsContentObserver = new SettingsContentObserver( new Handler() );
        this.getApplicationContext().getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                mSettingsContentObserver );
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Volume service destroyed");

        getContentResolver().unregisterContentObserver(mSettingsContentObserver);
    }

    public class SettingsContentObserver extends ContentObserver {

        public SettingsContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
//            boolean running = PreferenceManager.getDefaultSharedPreferences(StartService.this).getBoolean("running",false);
//            int counter = PreferenceManager.getDefaultSharedPreferences(StartService.this).getInt("counter",0);
//            Log.v("mytag", "Settings change detected");
//            if(!running) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        PreferenceManager.getDefaultSharedPreferences(StartService.this)
//                                .edit().putInt("counter",0).apply();
//                        PreferenceManager.getDefaultSharedPreferences(StartService.this)
//                                .edit().putBoolean("running",false).apply();
//                    }
//                }, 1700);
//                PreferenceManager.getDefaultSharedPreferences(StartService.this)
//                        .edit().putBoolean("running",true).apply();
//
//            }
//
//            if(counter%2!=0)
//                startActivity(new Intent(StartService.this, MainActivity.class));
//            PreferenceManager.getDefaultSharedPreferences(StartService.this)
//                    .edit().putInt("counter",counter+1).apply();
//
//            Log.d("mytag",PreferenceManager.getDefaultSharedPreferences(StartService.this).getInt("counter",0)+" count now");
//
        }
    }
}
