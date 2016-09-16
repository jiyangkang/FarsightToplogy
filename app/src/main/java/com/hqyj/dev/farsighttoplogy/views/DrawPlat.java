package com.hqyj.dev.farsighttoplogy.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.hqyj.dev.farsighttoplogy.R;
import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.OnDisconnect;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.OnValueReceived;
import com.hqyj.dev.farsighttoplogy.system_config.KownModules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jiyangkang on 2016/9/12 0012.
 */
public class DrawPlat extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap bitmapRoot;
    private Rect rectBitmapRootOr, rectBitmapRootDst;

    private Rect dstRect;

    private Context mContext;

    private int type = 0x5A;

    private HashMap<Integer, Integer> which;

    private Rect[] rects;

    private Line[] lines;

    private Paint mPaint, linePaint;

    public DrawPlat(Context context) {
        this(context, null);
    }

    public DrawPlat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPlat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        holder = this.getHolder();
        holder.addCallback(this);

        which = new HashMap<>();
        toShowHash = new HashMap<>();
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = mContext.getResources().getDisplayMetrics().heightPixels;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        dstRect = new Rect(0, 0, width, height * 3 / 4);

        setType('Z');

        int wRoot = bitmapRoot.getWidth();
        int hRoot = bitmapRoot.getHeight();
        rectBitmapRootOr = new Rect(0, 0, wRoot, hRoot);
        int wR = width / 12;
        int hR = hRoot * wR / hRoot;
        rectBitmapRootDst = new Rect(dstRect.left + dstRect.width() / 2 - wR / 2,
                dstRect.top + 50, dstRect.left + dstRect.width() / 2 + wR / 2,
                dstRect.top + 50 + hR);

        setRect();
        setLine();

        for (int i = 0; i < rects.length; i++) {
            disconnect(i);
        }

        setModules();

    }

    int count = 0;
    private final String TAG = DrawPlat.class.getSimpleName();
    private HashMap<Integer, String[]> toShowHash;

    private int whichIsBlank() {
        for (int i = 0; i < count; i++) {
            if (toShowHash.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private void setModules() {

        HashMap<Integer, Module> moduleHashMap = KownModules.getKownModules().getZigbeeModuleHash();
        for (Object o : moduleHashMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Integer id = (Integer) entry.getKey();
            Module module = (Module) entry.getValue();

            Log.d(TAG, "setModules: " + id);
            module.setOnValueReceived(new OnValueReceived() {

                @Override
                public void onValueReceived(String[] toShow, int Id) {
                    if (which.get(Id) != null) {
                        toShowHash.remove(which.get(Id));
                        toShowHash.put(which.get(Id), toShow);
                    } else {
                        int w = whichIsBlank();
                        if (w == -1) {
                            setLine(count, true);
                            which.put(Id, count);
                            toShowHash.put(count, toShow);

                            count++;
                            Log.d(TAG, String.format("count = %d", count));
                        } else {
                            setLine(w, true);
                            if (which.get(w) != null)
                                which.remove(w);
                            which.put(Id, w);
                            if (toShowHash.get(w) != null)
                                toShowHash.remove(w);
                            toShowHash.put(w, toShow);
                        }
                    }

                }

            });

            module.setOnDisconnect(new OnDisconnect() {
                @Override
                public void onDisconnect(int Id) {
                    if (which.get(Id) != null) {
                        disconnect(which.get(Id));
                        toShowHash.remove(which.get(Id));
                        which.remove(Id);
                        postInvalidate();
                    }
                }
            });
        }
    }


    /**
     * 设置网络
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
        setBitmapRoot(type);
    }

    public void setBitmapRoot(int type) {
        switch (type) {
            case 0x5A:
                this.bitmapRoot = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.zhome);
                break;
            case 0x57:
                this.bitmapRoot = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.whome);
                break;
            case 0x49:
                this.bitmapRoot = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.ihome);
                break;
            case 'B':
                this.bitmapRoot = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.bhome);
                break;
            default:
                break;
        }
    }


    public void setRect() {
        rects = new Rect[12];
        int w = rectBitmapRootDst.width();
        int h = rectBitmapRootDst.height();
        for (int i = 0; i < rects.length; i++) {
            if (i - rects.length / 2 >= 0) {
                rects[i] = new Rect(i * w + w / 7,
                        dstRect.bottom - (i - rects.length / 2 + 2) * 5 * h / 7 + 15,
                        i * w + w * 6 / 7,
                        dstRect.bottom - (i - rects.length / 2 + 1) * 5 * h / 7 + 15);
            } else {
                rects[i] = new Rect(i * w + w / 7,
                        dstRect.bottom - (Math.abs(i - rects.length / 2) + 1) * 5 * h / 7 + 15,
                        i * w + w * 6 / 7,
                        dstRect.bottom - (Math.abs(i - rects.length / 2)) * 5 * h / 7 + 15);
            }
        }
    }

    public void setLine(int i, boolean isConnect) {
        if (isConnect) {
            lines[i] = new Line(
                    new float[]{rectBitmapRootDst.left + rectBitmapRootDst.width() / 2, rectBitmapRootDst.bottom},
                    new float[]{rects[i].left + rects[i].width() / 2, rects[i].top});
        } else {
            lines[i] = null;
        }
    }

    public void setLine() {
        lines = new Line[12];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line(
                    new float[]{rectBitmapRootDst.left + rectBitmapRootDst.width() / 2, rectBitmapRootDst.bottom},
                    new float[]{rects[i].left + rects[i].width() / 2, rects[i].top});
        }
    }

    public void disconnect(int which) {
//        setRect(which, false);
        setLine(which, false);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        mPaint.setColor(Color.argb(0xff, 0x1d, 0xdd, 0xdd));
//        canvas.drawRect(dstRect, mPaint);
//        canvas.drawBitmap(bitmapRoot, rectBitmapRootOr, rectBitmapRootDst, mPaint);
//        for (int i = 0; i < rects.length; i++) {
//            if (rects[i] != null) {
//                Rect r = rects[i];
//                mPaint.setColor(Color.BLACK);
//                mPaint.setTextSize(12);
//                String s1, s2, s3;
//                if (toShowHash.get(i) != null) {
//                    s1 = toShowHash.get(i)[0];
//                    s2 = toShowHash.get(i)[1];
//                    s3 = toShowHash.get(i)[2];
//                    canvas.drawText(s1, r.left, r.top + 5, mPaint);
//                    canvas.drawText(s2, r.left, r.top + 25, mPaint);
//                    canvas.drawText(s3, r.left, r.top + 45, mPaint);
//                }
//            }
//        }
//
//        for (Line l : lines) {
//            if (l != null)
//                canvas.drawLine(l.getStart()[0], l.getStart()[1], l.getEnd()[0], l.getEnd()[1], linePaint);
//        }
//
//    }

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
            height = getPaddingTop() + dstRect.height() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
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
        }
        return super.onTouchEvent(event);
    }

    private SurfaceHolder holder;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        threadOn = true;
        new DrawThread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        threadOn = false;
    }

    private boolean threadOn = false;

    private class DrawThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadOn) {
                Canvas canvas = holder.lockCanvas();
                canvas.drawRect(dstRect, new Paint(Color.BLACK));
                canvas.drawBitmap(bitmapRoot, rectBitmapRootOr, rectBitmapRootDst, mPaint);
                mPaint.setColor(Color.argb(0xff, 0x00, 0xdd, 0xff));
                mPaint.setColor(Color.WHITE);
                synchronized (toShowHash) {
                    for (int i = 0; i < rects.length; i++) {

                        if (toShowHash.get(i) != null) {
                            String s1 = toShowHash.get(i)[0].toString();
                            String s2 = toShowHash.get(i)[1].toString();
                            String s3 = toShowHash.get(i)[2].toString();

                            canvas.drawText(s1, rects[i].left, rects[i].top + 15, mPaint);
                            canvas.drawText(s2, rects[i].left, rects[i].top + 35, mPaint);
                            canvas.drawText(s3, rects[i].left, rects[i].top + 55, mPaint);
                        }
                    }
                }
                holder.unlockCanvasAndPost(canvas);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
