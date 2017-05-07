package com.github.codeloop.braillo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.github.codeloop.braillo.controllers.GestureListener;
import com.github.codeloop.braillo.controllers.PageAdapter;
import com.github.codeloop.braillo.controllers.ScrollListener;
import com.github.codeloop.braillo.customviews.BrailleView;
import com.github.codeloop.braillo.customviews.ScrollBar;
import com.github.codeloop.braillo.models.Chat;
import com.github.codeloop.braillo.utils.PatternMapper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

/**
 * <p>
 * Created by Angad Singh on 6/5/17.
 * </p>
 */

public class ChatActivity extends Activity {
    private int matrix[][];
    private String roomName;
    private String deviceId;
    private TextToSpeech t1;
    private ViewPager viewPager;

    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<Character> arrayList;
    PageAdapter pageAdapter;
    ArrayList<String> allData;
    int listPointer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);

        allData =  new ArrayList<>();

        if(getIntent().getStringExtra("room")!=null) {
            roomName = getIntent().getStringExtra("room");
        }
        else {
            roomName = " default";
        }
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
        ScrollBar scrollBar = (ScrollBar) findViewById(R.id.scrollBar);
        viewPager = (ViewPager) findViewById(R.id.readPager);
        arrayList = new ArrayList<>();
        pageAdapter = new PageAdapter(this, arrayList);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                t1.speak(arrayList.get(position).toString(), TextToSpeech.QUEUE_FLUSH, null);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        edt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edt.setFocusable(true);
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
                t1.speak(resp, TextToSpeech.QUEUE_FLUSH, null);
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
                String message = edt.getText().toString();
                if(!message.equals("")) {
                    reference.push().setValue(new Chat(message, deviceId,
                            System.currentTimeMillis()));
                    t1.speak("Message Sent "+message, TextToSpeech.QUEUE_FLUSH, null);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(50);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    v.vibrate(50);
                    edt.setText("");
                }
            }

            @Override
            public void onSwipeDown(int fingers) {
                Log.e("GEST","Swipe Down:"+fingers);
                t1.speak("Pad Cleared", TextToSpeech.QUEUE_FLUSH, null);
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

        scrollBar.setScrollListener(new ScrollListener() {
            @Override
            public void onScrollUp() {
                Log.e("TAG","Scroll Up");
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                v.vibrate(50);

                if((listPointer-1)>=0 && listPointer<=allData.size()-1) {
                    Log.d("mytag", listPointer + " pointer up");
                    listPointer--;
                    Log.d("mytag", listPointer + " pointer up");
                    t1.speak("Next Message", TextToSpeech.QUEUE_FLUSH, null);
                    updatePager(allData.get(listPointer));

                }
            }

            @Override
            public void onScrollDown() {
                Log.e("TAG","Scroll Down");
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                v.vibrate(50);
                if(listPointer>=0&&(listPointer+1)<=allData.size()-1) {
                    listPointer++;
                    Log.d("mytag", listPointer + " pointer down");
                    t1.speak("Previous Message", TextToSpeech.QUEUE_FLUSH, null);
                    updatePager(allData.get(listPointer));
                }

            }
        });
    }

    private void listenToClient(){

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("user").getValue().toString().compareTo(deviceId)!=0) {
                    updatePager(dataSnapshot.child("message").getValue().toString());
                    allData.add(dataSnapshot.child("message").getValue().toString());
                    listPointer = allData.size()-1;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updatePager(String msg){
        Log.d("mytag","msg "+msg);
        arrayList.clear();
        for (char c : msg.toLowerCase().toCharArray())
            arrayList.add(c);
        pageAdapter = new PageAdapter(ChatActivity.this, arrayList);
        viewPager.setAdapter(pageAdapter);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);

    }

}
