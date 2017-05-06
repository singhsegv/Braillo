package com.github.codeloop.braillo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.github.codeloop.braillo.customviews.BrailleView;
import com.github.codeloop.braillo.utils.GestureHandler;
import com.github.codeloop.braillo.utils.PatternMapper;

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
        final AppCompatEditText edt = (AppCompatEditText)findViewById(R.id.screen);
        final BrailleView keypad = (BrailleView)findViewById(R.id.keypad);

        edt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edt.setEnabled(true);
                return true;
            }
        });
        keypad.setGestureHandler(new GestureHandler() {
            @Override
            public void onSwipeRight(int fingers) {
                Log.e("GEST","Swipe Right:"+fingers);
                String str = edt.getText().toString();
                if(str.length() > 0) {
                    edt.setText(str.substring(0, str.length() - 1));
                }
                keypad.clear();
            }

            @Override
            public void onSwipeLeft(int fingers) {
                Log.e("GEST","Swipe Left:"+fingers);
                int[][] matrix = keypad.getMapping();
                String resp = PatternMapper.compare(matrix);
//                Toast.makeText(getBaseContext(), "Matches: "+PatternMapper.compare(matrix), Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onSwipeDown(int fingers) {
                Log.e("GEST","Swipe Down:"+fingers);
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
}
