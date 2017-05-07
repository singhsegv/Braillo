package com.github.codeloop.braillo.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.codeloop.braillo.controllers.DotListener;

/**
 * <p>
 * Created by Angad Singh on 7/5/17.
 * </p>
 */

public class ReaderView extends View {
    private int width, height;
    private RectF[] bounds;
    private int minDim;
    private Paint paint;
    private int mapping[][];
    private int radius;
    private float centerX, centerY;
    private boolean map[];

    private DotListener dotListener;

    public ReaderView(Context context) {
        super(context);
        initView(context);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        mapping = new int[3][2];
        map = new boolean[6];
        bounds = new RectF[6];
        for (int i = 0; i < bounds.length; i++) {
            bounds[i] = new RectF();
        }
        for (int i = 0; i < map.length; i++) {
            map[i] = false;
        }
        centerX = 0; centerY = 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        paint.setColor(Color.WHITE);

//        canvas.drawLine(width / 2, 0, width / 2, height, paint);
//
//        canvas.drawLine(0, height / 3, width, height / 3, paint);
//        canvas.drawLine(0, height - (height / 3), width, height - (height / 3), paint);

//        paint.setColor(Color.YELLOW);

//        X Grids
//        canvas.drawLine(0, (height / 2) - (height / 3), width, (height /2) - (height / 3), paint);
//        canvas.drawLine(0, (height / 2), width, (height / 2), paint);
//        canvas.drawLine(0, (height / 2) + (height / 3), width, (height /2) + (height / 3), paint);
//
//        Y Grids
//        canvas.drawLine(width / 4, 0, width / 4, height, paint);
//        canvas.drawLine((width / 2) + (width / 4), 0, (width / 2) + (width / 4), height, paint);

        canvas.drawColor(Color.WHITE);

        for(int i = 0 ; i < 3 ; i++) {
            for(int j = 0; j < 2; j++) {
                float x = (centerX * j) + (centerX / 2);
                float y = ((2 * i) + 1) * (centerY / 3);

                if(mapping[i][j] == 0) {
                    paint.setColor(Color.parseColor("#DDDDDD"));
                }
                else {
                    paint.setColor(Color.parseColor("#324986"));
                }
                canvas.drawCircle(x , y , radius, paint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        centerX = width / 2;
        centerY = height / 2;
        minDim = Math.min(width, height);
        radius = minDim / 10;

        bounds[0].set(0 ,0 ,width / 2, height / 3);                                 //First Coordinate
        bounds[1].set(width / 2, 0, width, height / 3);                             //Second Coordinate
        bounds[2].set(0, height / 3, width / 2, (height / 2) + (height / 6));       //Third Coordinate
        bounds[3].set(width / 2, height / 3, width, (height / 2) + (height / 6));   //Fourth Coordinate
        bounds[4].set(0, (height / 2) + (height / 6), width / 2, height);           //Fifth Coordinate
        bounds[5].set(width / 2, (height / 2) + (height / 6), width, height);       //Sixth Coordinate
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        int index = MotionEventCompat.getActionIndex(event);

        final float x;
        final float y;

        int points = event.getPointerCount();
        if (points > 1) {
            x = MotionEventCompat.getX(event, index);
            y = MotionEventCompat.getY(event, index);

        } else {
            x = MotionEventCompat.getX(event, index);
            y = MotionEventCompat.getY(event, index);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                                            break;

            case MotionEvent.ACTION_MOVE:   if(event.getPointerCount()==1) {
                                                for (int i = 0; i < bounds.length; i++) {
                                                    if (bounds[i].contains(x, y)) {
                                                        int row = i / 2;
                                                        int col = i % 2;
                                                        if (!map[i]) {
                                                            if (mapping[row][col] == 0) {
                                                                dotListener.onBoundIntercept(false);
                                                            } else {
                                                                dotListener.onBoundIntercept(true);
                                                            }
                                                        }
                                                        map[i] = true;
                                                    } else {
                                                        map[i] = false;
                                                    }
                                                }
                                            }
                                            break;

            case MotionEvent.ACTION_UP:
                                        break;

            default: break;
        }
        return super.onTouchEvent(event);
    }

    public void setMapping(int[][] mapping) {
        this.mapping = mapping;
    }

    public void setDotListener(DotListener dotListener) {
        this.dotListener = dotListener;
    }
}
