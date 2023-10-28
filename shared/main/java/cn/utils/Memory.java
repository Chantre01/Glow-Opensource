package cn.utils;

import com.sun.management.OperatingSystemMXBean;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class Memory implements Listenable {

    @Override
    public boolean handleEvents() {
        return true;
    }
    public static float totalMemorySize,usedMemorySize;
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        totalMemorySize = (float) mem.getTotalPhysicalMemorySize()/(1024*1024);
        usedMemorySize = (float) mem.getTotalPhysicalMemorySize()-mem.getFreePhysicalMemorySize()/(1024*1024);
    }


    public static float getMemory() {
        return totalMemorySize/usedMemorySize;
    }

}
