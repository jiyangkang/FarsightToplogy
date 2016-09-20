package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class Mpu9250Module extends Module {

    private Mpu9250Module() {
        super();
        setId('M');
        setName("九轴");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas != null) {
                    int value = 0;
                    for (int i = 0; i < datas[1]; i++) {
                        value = (value << 8) | (datas[datas[2] + i] & 0x00ff);
                    }
                    toshow = String.format("加速度：x:%d;y:%d;z:%d;角速度：x:%d;y:%d;z:%d;电量:%d%%",
                            ((datas[datas[2]] & 0x00ff) << 8) | datas[datas[2] + 1] & 0x00ff,
                            ((datas[datas[2] + 2] & 0x00ff) << 8) | (datas[datas[2] + 3] & 0x00ff),
                            ((datas[datas[2] + 4] & 0x00ff) << 8) | (datas[datas[2] + 5] & 0x00ff),
                            ((datas[datas[2] + 8] & 0x00ff) << 8) | (datas[datas[2] + 9] & 0x00ff),
                            ((datas[datas[2] + 10] & 0x00ff) << 8) | (datas[datas[2] + 11] & 0x00ff),
                            ((datas[datas[2] + 12] & 0x00ff) << 8) | (datas[datas[2] + 13] & 0x00ff),
                            (datas[datas[2] - 1] & 0x00ff) << 8
                    );
                    return toshow;
                }
                return null;
            }
        };
        operate = null;
    }

    public static Mpu9250Module getMpu9250Module() {
        return LightModuleHolder.mpu9250Module;
    }

    public static class LightModuleHolder {
        private static final Mpu9250Module mpu9250Module = new Mpu9250Module();
    }

}
