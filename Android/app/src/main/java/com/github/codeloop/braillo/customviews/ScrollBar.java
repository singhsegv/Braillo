package com.github.codeloop.braillo.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.codeloop.braillo.controllers.ScrollListener;

/**
 * <p>
 * Created by Angad Singh on 7/5/17.
 * </p>
 */

public class ScrollBar extends View {
    private ScrollListener scrollListener;
    private float delY, moveY;
    private int moves;

    public ScrollBar(Context context) {
        super(context);
        initView();
    }

    public ScrollBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.LTGRAY);
    }

    void initView() {
        delY = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        int index = MotionEventCompat.getActionIndex(event);

        final float y;

        int points = event.getPointerCount();
        if (points > 1) {
            y = MotionEventCompat.getY(event, index);

        } else {
            y = MotionEventCompat.getY(event, index);
        }

        switch (action) {
            case MotionEvent.ACTION_UP:
                if(scrollListener!=null && moves > 10) {
                    if(delY > 0) {
                        scrollListener.onScrollDown();
                    }
                    else {
                        scrollListener.onScrollUp();
                    }
                }
                moves = 0;
                delY = 0;
                moveY = 0;
                break;

            case MotionEvent.ACTION_DOWN:
                moveY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                delY = y - moveY;
                moveY = y;
                moves++;
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
