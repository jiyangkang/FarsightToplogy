package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class TouchModule extends Module{
    private TouchModule touchModule;

    private TouchModule(){
        super();
        setId('T'-0x20);
        setName("触摸");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas!=null){
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++){
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    String v = (value == 'c')?"关":"开";
                    toshow = String.format("触摸：%s, 电量：%d", v, datas[8] & 0x00ff);
                    return toshow;
                }
                return null;
            }
        };
        operate = null;
    }

    public static TouchModule getTouchModule (){
        return LightModuleHolder.touchModule;
    }
    public static class LightModuleHolder{
        private static final TouchModule touchModule = new TouchModule();
    }

}
