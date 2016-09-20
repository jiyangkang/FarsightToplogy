package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class GasModule extends Module{

    private GasModule(){
        super();
        setId('G');
        setName("气体");
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

    public static GasModule getGasModule(){
        return LightModuleHolder.gasModule;
    }
    public static class LightModuleHolder{
        private static final GasModule gasModule = new GasModule();
    }

}
