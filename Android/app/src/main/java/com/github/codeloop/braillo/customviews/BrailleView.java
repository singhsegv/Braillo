package com.github.codeloop.braillo.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.codeloop.braillo.R;
import com.github.codeloop.braillo.utils.GestureHandler;

/**
 * <p>
 * Created by Angad Singh on 6/5/17.
 * </p>
 */

public class BrailleView extends View {
    private int width, height;
    private int minDim;
    private long counter;
    private Context context;
    private Paint paint;
    private int matrix[][];
    private int radius;
    private int maxTouch;
    private float centerX, centerY;
    private float moveX, moveY;
    private RectF[] bounds;
    private float delX, delY;
    private boolean running = false;
    private Handler handler;

    private int clickCount = 0;
    private static final int MAX_DURATION = 500;

    private GestureHandler gestureHandler;

    public BrailleView(Context context) {
        super(context);
        initView(context);
    }

    public BrailleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        matrix = new int[3][2];
        bounds = new RectF[6];
        handler = new Handler();
        for (int i = 0; i < bounds.length; i++) {
            bounds[i] = new RectF();
        }

        counter = 0; maxTouch = 0;
        centerX = 0; centerY = 0;
        moveX = 0; moveY = 0;
        clickCount = 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canvas.drawColor(context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
        }
        else {
            canvas.drawColor(context.getResources().getColor(R.color.colorPrimary));
        }

        paint.setColor(Color.WHITE);

        canvas.drawLine(width / 2, 0, width / 2, height, paint);

        canvas.drawLine(0, height / 3, width, height / 3, paint);
        canvas.drawLine(0, height - (height / 3), width, height - (height / 3), paint);

        paint.setColor(Color.YELLOW);

        //X Grids
        canvas.drawLine(0, (height / 2) - (height / 3), width, (height /2) - (height / 3), paint);
        canvas.drawLine(0, (height / 2), width, (height / 2), paint);
        canvas.drawLine(0, (height / 2) + (height / 3), width, (height /2) + (height / 3), paint);

        //Y Grids
        canvas.drawLine(width / 4, 0, width / 4, height, paint);
        canvas.drawLine((width / 2) + (width / 4), 0, (width / 2) + (width / 4), height, paint);

        for(int i = 0 ; i < 3 ; i++) {
            for(int j = 0; j < 2; j++) {
                float x = (centerX * j) + (centerX / 2);
                float y = ((2 * i) + 1) * (centerY / 3);

//                Log.e("COORD", x + ":" + y + ":" + i + ":" + j);
                if(matrix[i][j] == 0) {
                    paint.setColor(Color.WHITE);
                }
                else {
                    paint.setColor(Color.WHITE);
                }
                canvas.drawCircle(x , y , radius, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int points = event.getPointerCount();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP: if(Math.max(Math.abs(delX),Math.abs(delY)) < 2) {
                                            long time = System.currentTimeMillis() - counter;
                                            if(time < 250) {
                                                clickCount++;
                                                if(!running) {
                                                    running = true;
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            gestureHandler.onTouch(clickCount);
                                                            running = false;
                                                            clickCount = 0;
                                                        }
                                                    }, MAX_DURATION);
                                                }
                                                for(int i = 0; i < bounds.length; i++) {
                                                    if(bounds[i].contains(x, y)) {
                                                        Log.e("FOUND", "Found in "+(i+1)+" coordinate.");
                                                    }
                                                }

                                            }
                                        }
                                        else if(Math.abs(delX) > Math.abs(delY)) {
                                            if(gestureHandler!=null) {
                                                if(delX > 0) {
                                                    gestureHandler.onSwipeRight(maxTouch);
                                                }
                                                else {
                                                    gestureHandler.onSwipeLeft(maxTouch);
                                                }
                                            }
                                        }
                                        else {
                                            if(gestureHandler!=null) {
                                                if(delY > 0) {
                                                    gestureHandler.onSwipeDown(maxTouch);
                                                }
                                                else {
                                                    gestureHandler.onSwipeUp(maxTouch);
                                                }
                                            }
                                        }

                                        counter = 0;
                                        maxTouch = 0;
                                        delX = 0; delY = 0;
                                        moveX = 0; moveY = 0;
                                        break;

            case MotionEvent.ACTION_DOWN:   counter = System.currentTimeMillis();
                                            maxTouch = Math.max(maxTouch, points);
                                            moveX = x;
                                            moveY = y;

                                            clickCount++;
                                            break;

            case MotionEvent.ACTION_MOVE:   delX = x - moveX;
                                            delY = y - moveY;
                                            moveX = x;
                                            moveY = y;
                                            counter++;
                                            maxTouch = Math.max(maxTouch, points);
                                            break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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

    public void setGestureHandler(GestureHandler gestureHandler) {
        this.gestureHandler = gestureHandler;
    }
}
