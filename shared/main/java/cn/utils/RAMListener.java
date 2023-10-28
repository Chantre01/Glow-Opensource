package cn.utils;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class RAMListener implements Listenable {

    @Override
    public boolean handleEvents() {
        return true;
    }

    public static float maxMemorySize = 0,usedMemorySize = 0;
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        maxMemorySize = (float) memoryUsage.getMax()/(1024*1024);
        usedMemorySize = (float) memoryUsage.getUsed()/(1024*1024);
    }

    public static float getMemory() {
        return usedMemorySize/maxMemorySize;
    }


}
