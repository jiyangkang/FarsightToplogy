package com.hqyj.dev.farsighttoplogy.tools;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by jiyangkang on 2016/9/12 0012.
 */
public class DataTools {

    public static final byte HEAD = 0x00;
    public static final byte LENGTH = 0x01;
    public static final byte OFFSET = 0x02;
    public static final byte DATATYPE = 0x03;
    public static final byte NETTYPE = 0x04;
    public static final byte DEVICEADDR_H = 0x05;
    public static final byte DEVICEADDR_L = 0x06;
    public static final byte DEVICETYPE = 0x07;
    public static final byte DATA = 0x08;
    public static final byte MATA = 0x09;


    public static final byte HEAD_RECEIVE = 0x21;
    public static final byte CTRLDATA = 0x01;
    public static final byte NORMALDATA = 0x00;


    public static final int ERROR = -9999;

    public static ArrayBlockingQueue<byte[]> getFromUart = new ArrayBlockingQueue<>(128);
    public static ArrayBlockingQueue<byte[]> getFromSoap = new ArrayBlockingQueue<>(64);
    public static ArrayBlockingQueue<byte[]> sendToUart = new ArrayBlockingQueue<>(128);
    public static ArrayBlockingQueue<byte[]> sendToSoap = new ArrayBlockingQueue<>(64);


    public static final byte[] ENDTHREAD = {'E','N','D'};
    public static final byte[] ERRORCODE = {'E','R','R','O','R'};

}
