package com.github.codeloop.braillo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.codeloop.braillo.customviews.BrailleView;
import com.github.codeloop.braillo.utils.GestureHandler;

/**
 * <p>
 * Created by Angad Singh on 6/5/17.
 * </p>
 */

public class ChatActivity extends Activity {

    private int matrix[][];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        matrix = new int[3][2];
        BrailleView keypad = (BrailleView)findViewById(R.id.keypad);
        keypad.setGestureHandler(new GestureHandler() {
            @Override
            public void onSwipeRight(int fingers) {
                Log.e("GEST","Swipe Right:"+fingers);
            }

            @Override
            public void onSwipeLeft(int fingers) {
                Log.e("GEST","Swipe Left:"+fingers);
            }

            @Override
            public void onSwipeUp(int fingers) {
                Log.e("GEST","Swipe Up:"+fingers);
            }

            @Override
            public void onSwipeDown(int fingers) {
                Log.e("GEST","Swipe Down:"+fingers);
            }

            @Override
            public void onTouch(int counts) {
                Log.e("GEST","Touched:"+(counts/2));
            }
        });
    }
}
