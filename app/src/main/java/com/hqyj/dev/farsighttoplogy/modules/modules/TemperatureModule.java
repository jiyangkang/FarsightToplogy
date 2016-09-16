package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 *
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class TemperatureModule extends Module{
    private TemperatureModule temperatureModule;

    private TemperatureModule(){
        super();
        setId('T');
        show = new Show() {
            @Override
            public String[] setShow(byte[] datas) {
                String[] toshow;

                if (datas!=null){

                    String string1 = String.format("温度：%d℃，湿度：%d %%", datas[9], datas[10]);
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

    public static TemperatureModule getTemperatureModule (){
        return LightModuleHolder.temperatureModule;
    }
    public static class LightModuleHolder{
        private static final TemperatureModule temperatureModule = new TemperatureModule();
    }

}
