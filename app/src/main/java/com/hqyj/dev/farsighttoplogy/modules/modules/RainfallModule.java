package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class RainfallModule extends Module{

    private RainfallModule(){
        super();
        setId('R');
        setName("雨滴");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas!=null){
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++){
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    String v = value == 'c'?"无雨":"有雨";
                    toshow = String.format("雨滴：%s, 电量：%d",v, datas[8] & 0x00ff);
                    return toshow;
                }
                return null;
            }
        };
        operate = null;
    }

    public static RainfallModule getRainfallModule(){
        return LightModuleHolder.rainfallModule;
    }
    public static class LightModuleHolder{
        private static final RainfallModule rainfallModule = new RainfallModule();
    }

}
