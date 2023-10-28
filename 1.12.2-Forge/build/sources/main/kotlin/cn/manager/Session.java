package cn.manager;

import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;

public class Session extends MinecraftInstance implements Listenable {
    private final MSTimer msTimer = new MSTimer();
    private static int s, m, h, kills;
    private ServerData serverData;
    private EntityPlayer entity;

    @Override
    public boolean handleEvents() {
        return true;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.getCurrentServerData() == null || mc.isIntegratedServerRunning()) return;
        //Session Time
        if (serverData != mc.getCurrentServerData()) {
            s=0;
            m=0;
            h=0;
            kills=0;
            msTimer.reset();
            serverData = mc2.getCurrentServerData();
        }

        if (msTimer.hasTimePassed(1000)) {
            s++;
            if (s==60) {
                m++;
                s=0;
            }
            if (m==60) {
                h++;
                m=0;
            }
            msTimer.reset();
        }

        //Session Kills
        if (entity != null) {
            if (mc2.player.isDead) {
                entity = null;
                return;
            }
            if (entity.isDead) {
                kills++;
                entity = null;
            }
        }
    }

    @EventTarget
    public void onAttack(AttackEvent event) {
        if (event.getTargetEntity() instanceof EntityPlayer)
            entity = (EntityPlayer) event.getTargetEntity();
    }

    public static String getTime() {
        String ss,mm,hh;
        if (s<10) ss = "0" + s; else ss = String.valueOf(s);
        if (m<10) mm = "0" + m; else mm = String.valueOf(m);
        if (h<10) hh = "0" + h; else hh = String.valueOf(h);
        return hh+":"+mm+":"+ss;
    }

    public static String getKills() {
        return String.valueOf(kills);
    }
}
