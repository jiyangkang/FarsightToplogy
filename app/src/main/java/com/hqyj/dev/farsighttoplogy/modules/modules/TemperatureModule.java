package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class TemperatureModule extends Module {
    private TemperatureModule temperatureModule;

    private TemperatureModule() {
        super();
        setId('T');
        setName("温湿度");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas != null) {
                    toshow = String.format("温度：%d℃，湿度：%d %%,电量：%d", datas[datas[2]], datas[datas[2] + 1], datas[datas[2] - 1] & 0x00ff);
                    return toshow;
                }
                return null;
            }
        };
        operate = null;
    }

    public static TemperatureModule getTemperatureModule() {
        return LightModuleHolder.temperatureModule;
    }

    public static class LightModuleHolder {
        private static final TemperatureModule temperatureModule = new TemperatureModule();
    }

}
