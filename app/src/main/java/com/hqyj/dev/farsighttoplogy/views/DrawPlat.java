package com.hqyj.dev.farsighttoplogy.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.OnDisconnect;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.OnValueReceived;
import com.hqyj.dev.farsighttoplogy.system_config.KownModules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Draw Views for topology
 * Created by jiyangkang on 2016/9/14 0014.
 */
public class DrawPlat extends SurfaceView implements SurfaceHolder.Callback {

    private Context mContext;
    private SurfaceHolder holder;

    private Paint mPaint;
    private Rect dstRect;

    private Circular root;

    private HashMap<Integer, Module> hashMap;

    private HashMap<Integer, Line> lines;

    private HashMap<Integer, Circular> circulars;

    private HashMap<Module, Integer> modules;

    public DrawPlat(Context context) {
        this(context, null);
    }

    public DrawPlat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPlat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int h = context.getResources().getDisplayMetrics().heightPixels;
        holder = this.getHolder();
        holder.addCallback(this);

        dstRect = new Rect(0, 0, w * 8 / 8, h * 7 / 8);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);

        hashMap = KownModules.getKownModules().getZigbeeModuleHash();
    }


    private void setModules() {
        modules = new HashMap<>();

        for (Object o : hashMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Module module = (Module) entry.getValue();
            module.setOnValueReceived(new OnValueReceived() {
                @Override
                public void onValueReceived(String toShow, int id) {
                    Module m = hashMap.get(id);
                    if (modules.get(m) == null) {
                        for (int i = 0; i < 12; i++) {
                            if (circulars.get(i) == null) {
                                modules.put(m, i);
                                Circular c = new Circular(new float[]{dstRect.left + 250, dstRect.top + 35 + 36 * i}, 18, m.getName(), mPaint);
                                c.setTextToShow(toShow);
                                c.setDisconnect(false);
                                circulars.put(i, c);
                                Line l = new Line(root.getCenter(), c.getCenter(), mPaint);
                                l.setDisconnect(false);
                                lines.put(i, l);
                                break;
                            }
                        }

                    } else {
                        circulars.get(modules.get(m)).setDisconnect(false);
                        circulars.get(modules.get(m)).setTextToShow(toShow);
                        lines.get(modules.get(m)).setDisconnect(false);
                    }
                }
            });

            module.setOnDisconnect(new OnDisconnect() {
                @Override
                public void onDisconnect(int id) {
                    Module m = hashMap.get(id);
                    if (modules.get(m) != null) {
                        int i = modules.get(m);
                        modules.remove(m);
                        circulars.get(i).setDisconnect(true);

                        lines.get(i).setDisconnect(true);
                    }
                }
            });
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        circulars = new HashMap<>();
        lines = new HashMap<>();
        setModules();
        root = new Circular(new float[]{dstRect.left + 55, dstRect.top + dstRect.height() / 2},
                25, "根", mPaint);
        threadOn = true;

        new DrawNode().start();
    }


//    private class TestThread extends Thread {
//        @Override
//        public void run() {
//            super.run();
//            Circular circular;
//            Line line;
//            for (int i = 0; i < 12; i++) {
//                circular = new Circular(new float[]{dstRect.left + 100, dstRect.top + 25+ 36 * i},
//                        18, mPaint);
//                circulars.put(i, circular);
//                line = new Line(root.getCenter(), circular.getCenter(), mPaint);
//                lines.put(i, line);
//            }
//            for (int i = 0; i < 12; i++) {
//                circular = circulars.get(i);
//                float[] oldCenter = circular.getCenter();
//                float[] newCenter = new float[]{dstRect.left +100 + 50 * i, oldCenter[1]};
//
//                circular.setCenter(newCenter);
//                line = lines.get(i);
//                line.setEndXY(circular.getCenter());
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    private boolean threadOn = false;
    private final String TAG = DrawPlat.class.getSimpleName();


    private class DrawNode extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadOn) {

                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(mPaint.getColor());


                if (circulars != null && lines != null) {
                    for (int i = 0; i < 12; i++) {
                        if (circulars.get(i) != null && lines.get(i) != null) {
                            if (lines.get(i).isDisconnect()) {
                                lines.get(i).unDrawLine(canvas);
                                lines.remove(i);
                            } else {
                                lines.get(i).drawLine(canvas);
                            }
                            if (circulars.get(i).isDisconnect()) {
                                circulars.get(i).unDrawCircular(canvas);
                            } else {
                                circulars.get(i).drawCircular(canvas);
                            }
                        }
                    }
                }
                if (root != null) {
                    root.drawCircular(canvas);
                }
                holder.unlockCanvasAndPost(canvas);


            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        threadOn = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] XY = new float[]{event.getX(), event.getY()};
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getPaddingLeft() + getPaddingRight() + dstRect.width();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getPaddingTop() + getPaddingBottom() + dstRect.height();
        }
        setMeasuredDimension(width, height);
    }
}
