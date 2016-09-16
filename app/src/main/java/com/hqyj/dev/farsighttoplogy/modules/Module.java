package com.hqyj.dev.farsighttoplogy.modules;

import com.hqyj.dev.farsighttoplogy.modules.interfaces.OnDisconnect;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.OnValueReceived;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Operate;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/12 0012.
 */
public abstract class Module {
    private String name;//节点名字--未知为默认节点
    private int type;
    private int netType;
    private byte[] datas;

    private byte[] address;

    public Operate operate;

    public Show show;
    private int number = 20;

    private SelfReduceThread selfReduceThread;

    private boolean threadOn = false;

    private String[] toShow;

    public Module(){
        threadOn = true;
        selfReduceThread = new SelfReduceThread();
        selfReduceThread.start();
    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    private OnValueReceived onValueReceived;
    private OnDisconnect onDisconnect;

    public void setOnDisconnect(OnDisconnect onDisconnect) {
        this.onDisconnect = onDisconnect;
    }

    public void setOnValueReceived(OnValueReceived onValueReceived) {
        this.onValueReceived = onValueReceived;
    }

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setValue(byte[] datas){
        setDatas(datas);
        number = 20;
        toShow = show.setShow(datas);
        if (onValueReceived!= null && toShow != null){
            onValueReceived.onValueReceived(toShow, id);
        }
    }

    public byte[] getDatas() {
        return datas;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    private class SelfReduceThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(threadOn){
                if (number != 0){
                    number --;
                }else {
                    if (onDisconnect != null){
                        onDisconnect.onDisconnect(id);
                    }
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
