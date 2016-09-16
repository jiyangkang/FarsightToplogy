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
    private LampModule lampModule;

    private LampModule() {
        super();
        setId('l');
        show = new Show() {
            @Override
            public String[] setShow(byte[] datas) {
                String[] toshow;

                if (datas != null) {
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++) {
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    String v = value == 0 ? "关" : "开";
                    String string1 = String.format("灯：%s", v);
                    String string2 = String.format("地址：%02X %02X", datas[6], datas[7]);
                    setAddress(new byte[]{datas[6], datas[7]});
                    String string3 = String.format("电量：%d", datas[8] & 0x00ff);
                    toshow = new String[]{string1, string2, string3};
                    return toshow;
                }
                return null;
            }
        };

        operate = new Operate() {
            @Override
            public void sendCmd(int which) {
                byte[] datas = getDatas();
                if (datas != null) {
                    byte[] send = new byte[datas.length + 1];
                    System.arraycopy(datas, 0, send, 0, datas.length - 1);
                    send[1] = 0x02;
                    send[3] = 0x01;
                    switch (which) {
                        case 1:
                            send[datas[datas[2]]] = 0x31;
                            send[datas[datas[2]] + 1] = 0x01;
                            break;
                        case 2:
                            send[datas[datas[2]]] = 0x31;
                            send[datas[datas[2]] + 1] = 0x02;
                            break;
                        case 3:
                            send[datas[datas[2]]] = 0x31;
                            send[datas[datas[2]] + 1] = 0x03;
                            break;
                        case 4:
                            send[datas[datas[2]]] = 0x31;
                            send[datas[datas[2]] + 1] = 0x04;
                            break;
                        case 5:
                            send[datas[datas[2]]] = 0x30;
                            send[datas[datas[2]] + 1] = 0x04;
                            break;
                        default:

                            break;
                    }

                    byte[] check = new byte[datas.length];
                    System.arraycopy(send, 0, check, 0, check.length);
                    byte mate = MathTools.makeMate(check);
                    send[datas.length] = mate;
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
