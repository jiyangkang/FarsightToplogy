package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class InfredModule extends Module{

    private InfredModule(){
        super();
        setId('I');
        setName("人体红外");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas!=null){
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++){
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    String v = value == 'c' ?"无人":"有人";
                    toshow = String.format("红外：%s,电量：%d", v, datas[datas[2] - 1] & 0x00ff);
                    return toshow;
                }
                return null;
            }
        };
        operate = null;
    }

    public static InfredModule getInfredModule(){
        return LightModuleHolder.infredModule;
    }
    public static class LightModuleHolder{
        private static final InfredModule infredModule = new InfredModule();
    }

}
