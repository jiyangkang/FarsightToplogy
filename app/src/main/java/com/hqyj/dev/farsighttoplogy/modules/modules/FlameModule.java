package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class FlameModule extends Module{

    private FlameModule(){
        super();
        setId('F'-0x20);
        setName("火焰");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas!=null){
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++){
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    toshow = String.format("电压：%dmv,电量：%d", value, datas[datas[2] - 1] & 0x00ff);
                    return toshow;
                }
                return null;
            }
        };

        operate = null;
    }

    public static FlameModule getFlameModule(){
        return LightModuleHolder.flameModule;
    }
    public static class LightModuleHolder{
        private static final FlameModule flameModule = new FlameModule();
    }

}
