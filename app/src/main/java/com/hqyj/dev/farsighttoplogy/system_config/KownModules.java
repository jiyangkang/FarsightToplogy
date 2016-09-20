package com.hqyj.dev.farsighttoplogy.system_config;


import com.hqyj.dev.farsighttoplogy.modules.Module;
import com.hqyj.dev.farsighttoplogy.modules.modules.BMP180Module;
import com.hqyj.dev.farsighttoplogy.modules.modules.BeepModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.FanModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.FlameModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.ForgModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.GasModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.HeartBeatModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.InfredModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.LampModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.LightModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.Mpu9250Module;
import com.hqyj.dev.farsighttoplogy.modules.modules.PhotocatorModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.RainfallModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.RelayModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.TemperatureModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.TouchModule;
import com.hqyj.dev.farsighttoplogy.modules.modules.UltrasonicModule;

import java.util.HashMap;

/**
 * Created by jiyangkang on 2016/9/12 0012.
 */
public class KownModules {


    private KownModules(){
        zigbeeModuleHash = new HashMap<>();
        setZigbeeModuleHash((int) 'L', LightModule.getLightModule());
        setZigbeeModuleHash((int) 'G', GasModule.getGasModule());
        setZigbeeModuleHash((int) 'F', ForgModule.getForgModule());
        setZigbeeModuleHash((int) 'F' - 0x20, FlameModule.getFlameModule());
        setZigbeeModuleHash((int) 'T', TemperatureModule.getTemperatureModule());
        setZigbeeModuleHash((int) 'H', HeartBeatModule.getHeartBeatModule());
        setZigbeeModuleHash((int) 'U', UltrasonicModule.getUltrasonicModule());
        setZigbeeModuleHash((int) 'T' - 0x20, TouchModule.getTouchModule());
        setZigbeeModuleHash((int) 'P', PhotocatorModule.getPhotocatorModule());
        setZigbeeModuleHash((int) 'I', InfredModule.getInfredModule());
        setZigbeeModuleHash((int) 'R' ,RainfallModule.getRainfallModule());
        setZigbeeModuleHash((int) 'M', Mpu9250Module.getMpu9250Module());
        setZigbeeModuleHash((int) 'B', BMP180Module.getBMP180Module());
        setZigbeeModuleHash((int) 'l', LampModule.getLampModule());
        setZigbeeModuleHash((int) 'b', BeepModule.getBeepModule());
        setZigbeeModuleHash((int) 'f', FanModule.getFanModule());
        setZigbeeModuleHash((int) 'r', RelayModule.getRelayModule());
    }

    public static KownModules getKownModules(){
        return KownModulesHolder.kownModules;
    }

    public static class KownModulesHolder{
        private static final KownModules kownModules = new KownModules();

    }

    private HashMap<Integer, Module> zigbeeModuleHash;

    public HashMap<Integer, Module> getZigbeeModuleHash() {
        return zigbeeModuleHash;
    }

    public void setZigbeeModuleHash(Integer addr, Module module) {
        zigbeeModuleHash.put(addr, module);
    }


}
