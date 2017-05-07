package com.github.codeloop.braillo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.codeloop.braillo.controllers.GestureListener;
import com.github.codeloop.braillo.controllers.PageAdapter;
import com.github.codeloop.braillo.customviews.BrailleView;
import com.github.codeloop.braillo.models.Chat;
import com.github.codeloop.braillo.utils.PatternMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * <p>
 * Created by Angad Singh on 6/5/17.
 * </p>
 */

public class ChatActivity extends Activity {
    private int matrix[][];
    private String roomName;
    String deviceId;

    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<Character> arrayList;
    PageAdapter pageAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if(getIntent().getStringExtra("room")!=null)
            roomName = getIntent().getStringExtra("room");
        else
            roomName = " default";

        Log.d("mytag",roomName+" roomName");
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("room",roomName).apply();
        TelephonyManager tMgr =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = tMgr.getDeviceId();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child(roomName);
        listenToClient();

        matrix = new int[3][2];
        final AppCompatEditText edt = (AppCompatEditText)findViewById(R.id.screen);
        final BrailleView keypad = (BrailleView)findViewById(R.id.keypad);
        ViewPager viewPager = (ViewPager) findViewById(R.id.readPager);
        arrayList = new ArrayList<>();
        pageAdapter = new PageAdapter(this, arrayList);
        viewPager.setAdapter(pageAdapter);
//        readerView.setMapping(PatternMapper.A);
        edt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edt.setEnabled(true);
                return true;
            }
        });
        keypad.setGestureHandler(new GestureListener() {
            @Override
            public void onSwipeRight(int fingers) {
                Log.e("GEST","Swipe Right:"+fingers);
                String str = edt.getText().toString();
                if(str.length() > 0) {
                    edt.setText(str.substring(0, str.length() - 1));
                }
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                v.vibrate(50);
                keypad.clear();
            }

            @Override
            public void onSwipeLeft(int fingers) {
                Log.e("GEST","Swipe Left:"+fingers);
                int[][] matrix = keypad.getMapping();
                String resp = PatternMapper.compare(matrix);
                StringBuffer buff = new StringBuffer(edt.getText().toString());
                edt.setText(buff.append(resp));
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                v.vibrate(50);
                keypad.clear();
            }

            @Override
            public void onSwipeUp(int fingers) {
                Log.e("GEST","Swipe Up:"+fingers);
                reference.push().setValue(new Chat(edt.getText().toString(),deviceId,
                        System.currentTimeMillis()));
                edt.setText("");

            }

            @Override
            public void onSwipeDown(int fingers) {
                Log.e("GEST","Swipe Down:"+fingers);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                v.vibrate(50);
                keypad.clear();
            }

            @Override
            public void onTouch(int counts) {
                if(counts == 1) {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(200);
                }
                Log.e("GEST","Touched: "+counts);
            }
        });
    }

    private void listenToClient(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap:dataSnapshot.getChildren()) {

                    arrayList.clear();
                    for (char c : dataSnap
                            .child("message").getValue().toString().toCharArray())
                        arrayList.add(c);
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
