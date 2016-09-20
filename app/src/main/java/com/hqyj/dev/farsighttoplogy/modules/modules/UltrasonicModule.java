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
        setName("超声波");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas!=null){
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++){
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    toshow = String.format("超声波：%dmm, 电量：%d",value, datas[8] & 0x00ff);
                    return toshow;
                }
                return null;
            }
        };
        operate = null;
    }

    public static UltrasonicModule getUltrasonicModule(){
        return LightModuleHolder.ultrasonicModule;
    }
    public static class LightModuleHolder{
        private static final UltrasonicModule ultrasonicModule = new UltrasonicModule();
    }

}
