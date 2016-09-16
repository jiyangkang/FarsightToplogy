package com.hqyj.dev.farsighttoplogy.sevices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hqyj.dev.farsighttoplogy.tools.DataTools;
import com.hqyj.dev.farsighttoplogy.tools.UartTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiyangkang on 2016/9/12 0012.
 */
public class UartService extends Service {


    private final String TAG = UartService.class.getSimpleName();
    private boolean threadOn = false;
    private UartTools uartTools;
    private OutputStream outputStream;
    private InputStream inputStream;
    //    private String path = SystemConfig.getSystemConfig().getUartPort();
//    private int baudRate = SystemConfig.getSystemConfig().getBaudRate();
    private String path = "/dev/ttySAC3";
    private int baudRate = 115200;
    private WriteUart writeUart = null;
    private ReadUart readUart = null;
//    private SendToSoapThread sendToSoapThread = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            uartTools = new UartTools(new File(path), baudRate);
            outputStream = uartTools.getFileOutputStream();
            inputStream = uartTools.getFileInputStream();
            threadOn = true;
            if (readUart == null) {
                readUart = new ReadUart();
                readUart.start();
            }
            if (writeUart == null) {
                writeUart = new WriteUart();
                writeUart.start();
            }
//            if (sendToSoapThread == null) {
////                sendToSoapThread = new SendToSoapThread();
//            }
//            sendToSoapThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private class WriteUart extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadOn && !isInterrupted()) {
                try {
                    byte[] datas = DataTools.sendToUart.take();
                    if (datas == DataTools.ENDTHREAD) {
                        break;
                    }
                    if (datas != null) {
                        if (sendCmds(datas)){
                            Log.d(TAG, "sendCmds OK");
                        }else {
                            Log.d(TAG, "sendCmds FAIL");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean sendCmds(byte[] datas) {
        boolean result = true;
        try {
            if (outputStream != null) {
                outputStream.write(datas);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    private class ReadUart extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadOn && !isInterrupted()) {
                try {
                    Log.d(TAG, "run: in try");
                    if (inputStream == null) {
                        return;
                    }
                    byte[] buffer = new byte[512];
                    int size = inputStream.read(buffer);
                    Log.d(TAG, "run: after read" + size);
                    if (size <= 0) {
                        sleep(200);
                        continue;
                    }
                    byte[] toShow = new byte[size];
                    System.arraycopy(buffer, 0, toShow, 0, size);
//                    Log.d(TAG, String.format("%s", StringTools.changeIntoHexString(toShow, true)));

                    DataTools.getFromUart.put(toShow);

                } catch (Exception ignored) {
                    Log.d("UartRead", "Read Error occ");
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                }
            }

        }
    }


    public void closeUartPort() {
        if (uartTools != null) {
            uartTools.uartClose();
        }
        uartTools = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            DataTools.sendToUart.put(DataTools.ENDTHREAD);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadOn = false;
        readUart.interrupt();
        writeUart.interrupt();
//        sendToSoapThread.interrupt();
        readUart = null;
        writeUart = null;
//        sendToSoapThread = null;
        closeUartPort();
    }


}
