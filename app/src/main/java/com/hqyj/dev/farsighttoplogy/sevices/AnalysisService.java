package com.hqyj.dev.farsighttoplogy.sevices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.system_config.KownModules;
import com.hqyj.dev.farsighttoplogy.tools.DataTools;
import com.hqyj.dev.farsighttoplogy.tools.MathTools;
import com.hqyj.dev.farsighttoplogy.tools.StringTools;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jiyangkang on 2016/7/2 0002.
 */
public class AnalysisService extends Service {

    private final String TAG = AnalysisService.class.getSimpleName();
    private boolean threadOn = false;


    private byte[] bufferPut;
    private byte status;
    private int i_record;
    private AnalysisThread analysisThread = null;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bufferPut = new byte[1024];
        bytes = new ArrayList<>();
        status = DataTools.HEAD;
        threadOn = true;

        if (analysisThread == null) {
            analysisThread = new AnalysisThread();
            analysisThread.start();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (analysisThread != null) {
            analysisThread = null;
        }
    }

    private class AnalysisThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadOn) {
                try {
                    byte[] datas = DataTools.getFromUart.take();
                    analysisiData(datas);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ArrayList<Byte> bytes;

    int z = 0;
    int l = 0;
    int o = 0;

    private void analysisiData(byte[] buffer) {
        int size = buffer.length;
        Byte[] bufferB = new Byte[size];
        for (int i = 0; i < size; i++) {
            bufferB[i] = buffer[i];
        }
        Collections.addAll(bytes, bufferB);
        Log.d(TAG, "analysisiData: " + bytes.size());

        byte[] assss = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++){
            assss[i] = bytes.get(i);
        }
        Log.d(TAG, StringTools.changeIntoHexString(assss, true));

        int i = 0;

        while (i < bytes.size()){
            int b = bytes.get(i) & 0x00ff;
            switch (status){
                case DataTools.HEAD:
                    if (b == DataTools.HEAD_RECEIVE){
                        bufferPut = new byte[64];
                        bufferPut[DataTools.HEAD] = (byte) b;
                        status = DataTools.LENGTH;
                        z = i;
                        Log.d(TAG, "analysisiData: Head");
                    }
                    break;
                case DataTools.LENGTH:
                    if (b < 0x20){
                        bufferPut[DataTools.LENGTH] = (byte) b;
                        l = b;
                        status = DataTools.OFFSET;
                    }else {
                        status = DataTools.HEAD;
                        l = z =0;
                    }
                    break;
                case DataTools.OFFSET:
                    if (b < 0x20){
                        bufferPut[DataTools.OFFSET] = (byte) b;
                        status = DataTools.DATA;
                        o = b;
                    }else {
                        status = DataTools.HEAD;
                        l = o = z = 0;
                    }
                    break;
                case DataTools.DATA:
                    bufferPut[i-z] = (byte) b;
                    if (i-z == l + o){
                        byte[] toSend = new byte[l+o+1];
                        System.arraycopy(bufferPut, 0, toSend, 0, toSend.length);
                        if (MathTools.checkMata(toSend)){
                            for (int j = 0; j < i; j++){
                                bytes.remove(0);
                            }
                            i = 0;
                            status = DataTools.HEAD;
                            int id = toSend[DataTools.DEVICETYPE] & 0x00ff;
                            Module module;
                            if ((module = KownModules.getKownModules().getZigbeeModuleHash().get(id)) != null){
                                module.setValue(toSend);
                            }else {
                                Log.d(TAG, "analysisiData: unkown Module");
                            }
                            Log.d(TAG, "analysisiData: ### OK");
                        }else {
                            status = DataTools.HEAD;
                            Log.d(TAG, "analysisiData: ### Fail");
                        }
                    }
                    break;
                default:
                    break;
            }
            i++;
        }
        status = DataTools.HEAD;

    }
}
