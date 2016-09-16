package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class UltrasonicModule extends Module{
    private UltrasonicModule ultrasonicModule;

    private UltrasonicModule(){
        super();
        setId('U');
        show = new Show() {
            @Override
            public String[] setShow(byte[] datas) {
                String[] toshow;

                if (datas!=null){
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++){
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    String string1 = String.format("超声波：%dmm", value);
                    String string2 = String.format("地址：%02X %02X", datas[6], datas[7]);
                    setAddress(new byte[]{datas[6], datas[7]});
                    String string3 = String.format("电量：%d", datas[8] & 0x00ff);
                    toshow = new String[] {string1, string2, string3};
                    return toshow;
                }
                return null;
            }
        };
    }

    public static UltrasonicModule getUltrasonicModule(){
        return LightModuleHolder.ultrasonicModule;
    }
    public static class LightModuleHolder{
        private static final UltrasonicModule ultrasonicModule = new UltrasonicModule();
    }

}
