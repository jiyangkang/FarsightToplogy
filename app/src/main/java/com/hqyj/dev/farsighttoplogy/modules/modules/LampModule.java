package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;
import android.provider.Settings;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Operate;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;
import com.hqyj.dev.farsighttoplogy.tools.DataTools;
import com.hqyj.dev.farsighttoplogy.tools.MathTools;

/**
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class LampModule extends Module {

    private int value = 0;

    private LampModule() {
        super();
        setId('l');
        setName("灯");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas != null) {
                    for (int i = 0; i < 1; i++) {
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    String v = value == 'c' ? "关" : "开";
                    toshow = String.format("灯：%s,电量：%d", v, datas[8] & 0x00ff);
                    return toshow;
                }
                return null;
            }
        };

        operate = new Operate() {
            @Override
            public void sendCmd() {
                byte[] datas = getDatas();
                if (datas != null) {
                    byte[] send = new byte[datas.length];
                    System.arraycopy(datas, 0, send, 0, datas.length - 1);
                    send[1] = 0x02;
                    send[3] = 0x01;
                    switch (value) {
                        case 'o':
                            send[datas[datas[2]]] = 'c';
                            send[datas[datas[2] + 1]] = 'a';
                            break;
                        case 'c':
                            send[datas[datas[2]]] = 'o';
                            send[datas[datas[2] + 1]] = 'a';
                            break;
                        default:
                            break;
                    }

                    byte[] check = new byte[datas.length - 1];
                    System.arraycopy(send, 0, check, 0, check.length);
                    byte mate = MathTools.makeMate(check);
                    send[datas.length - 1] = mate;
                    try {
                        DataTools.sendToUart.put(send);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


    }

    public static LampModule getLampModule() {
        return LightModuleHolder.lampModule;
    }

    public static class LightModuleHolder {
        private static final LampModule lampModule = new LampModule();
    }

}
