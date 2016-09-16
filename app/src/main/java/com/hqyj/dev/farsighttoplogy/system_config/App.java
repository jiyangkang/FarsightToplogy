package com.hqyj.dev.farsighttoplogy.system_config;

import android.app.Application;

import org.xutils.x;

/**
 * Created by jiyangkang on 2016/9/12 0012.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
