package com.github.codeloop.braillo.controllers;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.codeloop.braillo.customviews.ReaderView;
import com.github.codeloop.braillo.utils.PatternMapper;

import java.util.ArrayList;

/**
 * <p>
 * Created by Angad Singh on 7/5/17.
 * </p>
 */

public class PageAdapter extends PagerAdapter {
    private ArrayList<Character> stream;
    private Context context;

    public PageAdapter(Context context, ArrayList<Character> stream) {
        this.context = context;
        this.stream = stream;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ReaderView view = new ReaderView(context);
        view.setMapping(PatternMapper.getArray(stream.get(position).toString()));
        view.setClickable(true);
        view.setDotListener(new DotListener() {
            @Override
            public void onBoundIntercept(boolean state) {
                if(state) {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    v.vibrate(100);
                }
                else {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(200);
                }
            }
        });
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return stream.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
