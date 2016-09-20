package com.hqyj.dev.farsighttoplogy.modules.modules;

import android.annotation.SuppressLint;

import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.interfaces.Show;

/**
 * Created by jiyangkang on 2016/9/13 0013.
 */
@SuppressLint("DefaultLocale")
public class BMP180Module extends Module {

    private BMP180Module() {
        super();
        setId('M');
        setName("气压传感器");
        show = new Show() {
            @Override
            public String setShow(byte[] datas) {
                String toshow;

                if (datas != null) {
                    toshow = String.format("大气压强：%f kPa,高度:%d m,电量：%d%%",
                            (double)((datas[datas[2] + 1] & 0x00ff) << 8 | datas[datas[2] + 2] & 0x00ff) / 100.00,
                            (datas[datas[2] + 1] & 0x00ff) << 8 | datas[datas[2] + 2] & 0x00ff,
                            (datas[datas[2] - 1] & 0x00ff) << 8
                    );
                    return toshow;
                }
                return null;
            }
        };

        operate = null;
    }

    public static BMP180Module getBMP180Module() {
        return LightModuleHolder.bmp180Module;
    }

    public static class LightModuleHolder {
        private static final BMP180Module bmp180Module = new BMP180Module();
    }

}
