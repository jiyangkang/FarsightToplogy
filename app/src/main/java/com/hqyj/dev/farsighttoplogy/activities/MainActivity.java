package com.hqyj.dev.farsighttoplogy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hqyj.dev.farsighttoplogy.R;
import com.hqyj.dev.farsighttoplogy.sevices.AnalysisService;
import com.hqyj.dev.farsighttoplogy.sevices.UartService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置全屏和无标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Intent uartIntent = new Intent(MainActivity.this, UartService.class);
        startService(uartIntent);

        Intent analysisIntent = new Intent(MainActivity.this, AnalysisService.class);
        startService(analysisIntent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, UartService.class));
        stopService(new Intent(MainActivity.this, AnalysisService.class));
    }
}
