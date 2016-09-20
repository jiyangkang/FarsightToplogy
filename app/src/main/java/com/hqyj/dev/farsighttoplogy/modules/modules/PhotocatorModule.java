package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class PhotocatorModule extends Module{

    private PhotocatorModule(){
        super();
        setId('P');
        setName("光电开关");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas!=null){
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++){
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    String v = value == 'c'?"关":"开";

                    toshow = String.format("光电开关：%s, 电量：%d", v, datas[datas[2] - 1] & 0x00ff);
                    return toshow;
                }
                return null;
            }
        };
        operate = null;
    }

    public static PhotocatorModule getPhotocatorModule(){
        return LightModuleHolder.photocatorModule;
    }
    public static class LightModuleHolder{
        private static final PhotocatorModule photocatorModule = new PhotocatorModule();
    }

}
