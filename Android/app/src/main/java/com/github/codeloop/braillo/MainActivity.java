package com.github.codeloop.braillo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.codeloop.braillo.models.Chat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.glxn.qrgen.android.QRCode;



public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    int counter;
    LinearLayout linearLayout;
    Handler handler;
    boolean isRunning;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(), StartService.class));

        if(PreferenceManager.getDefaultSharedPreferences(this).contains("room")){
            Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
            intent.putExtra("room",
                    PreferenceManager.getDefaultSharedPreferences(this).getString("room","default"));
            startActivity(intent);
            finish();
        } else {

            linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
            linearLayout.setOnTouchListener(this);
            counter = 0;
            isRunning = false;
            handler = new Handler();
            checkPermissions();
            firebaseDatabase = FirebaseDatabase.getInstance();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==20){
            String phoneData = data.getStringExtra("phonedata");
            TelephonyManager tMgr =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

            //send firebase request from qrcoder reader user
            String roomName = phoneData.split(",")[1];
            reference=firebaseDatabase.getReference().child(roomName);
            reference.push().setValue(new Chat("OK",tMgr.getDeviceId(),System.currentTimeMillis()));
            Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
            intent.putExtra("room",roomName);
            startActivity(intent);
        }

    }

    private void startScanner(){
        Intent I = new Intent(MainActivity.this, QrReaderActivity.class);
        startActivityForResult(I, 20);
    }

    private void checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {


        } else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA}, 10);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void generateQR(String data){
        String roomNameForQrGenerator = String.valueOf(System.currentTimeMillis());
        Bitmap myBitmap = QRCode.from(data+","+roomNameForQrGenerator).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.img);
        myImage.setVisibility(View.VISIBLE);
        myImage.setImageBitmap(myBitmap);

        listenToClient(roomNameForQrGenerator);
    }


    private void listenToClient(final String roomName){
        reference=firebaseDatabase.getReference().child(roomName);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                Log.d("mytag",roomName);
                intent.putExtra("room",roomName);
                startActivity(intent);
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action=event.getActionMasked();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                counter++;
                if(!isRunning) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkForTaps(counter);
                            counter = 0;
                            Log.d("mytag", "reset");
                            isRunning=false;
                        }
                    }, 700);
                    isRunning=true;
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    private void checkForTaps(int count){
        Log.d("mytag",count+" counter");
        if(count==3)
            startScanner();
        else if(count==2) {

            TelephonyManager tMgr =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            generateQR(tMgr.getDeviceId());
        }
    }
}
