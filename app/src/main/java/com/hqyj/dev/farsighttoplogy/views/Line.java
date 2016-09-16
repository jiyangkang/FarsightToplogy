package com.hqyj.dev.farsighttoplogy.views;

/**
 * Created by jiyangkang on 2016/9/12 0012.
 */
public class Line {

    private float[] start;
    private float[] end;

    public Line(){

    }

    public Line(float[] start, float[] end) {
        this.start = start;
        this.end = end;
    }


    public float[] getStart() {
        return start;
    }

    public void setStart(float[] start) {
        this.start = start;
    }

    public float[] getEnd() {
        return end;
    }

    public void setEnd(float[] end) {
        this.end = end;
    }
}
