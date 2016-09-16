package com.hqyj.dev.farsighttoplogy.tools;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jiyangkang on 2016/9/12 0012.
 */
@SuppressWarnings("JniMissingFunction")
public class UartTools {

    private FileDescriptor mFd;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    /**
     * 构造器
     * @param device 设备
     * @param baudRate 波特率
     * @throws IOException
     */
    public UartTools(File device, int baudRate) throws IOException {
        mFd = uartOpen(device.getAbsolutePath(), baudRate);
        if (mFd == null){
            throw new IOException();
        }
        fileInputStream = new FileInputStream(mFd);
        fileOutputStream = new FileOutputStream(mFd);
    }


    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public OutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public native FileDescriptor uartOpen(String file, int baudRate);
    public native int uartClose();

    static {
        System.loadLibrary("uart_toplogy");
    }
}
