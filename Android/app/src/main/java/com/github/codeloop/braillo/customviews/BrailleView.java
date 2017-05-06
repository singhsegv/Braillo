package com.github.codeloop.braillo.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * <p>
 * Created by Angad Singh on 6/5/17.
 * </p>
 */

public class BrailleView extends View {
    private int width, height;
    private int minDim;
    private int counter;

    public BrailleView(Context context) {
        super(context);
    }

    public BrailleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                                            break;

            case MotionEvent.ACTION_UP:
                                        break;

            case MotionEvent.ACTION_MOVE:
                                            break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        minDim = Math.min(width, height);
    }
}
