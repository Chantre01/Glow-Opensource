package net.ccbluex.liquidbounce.features.module.modules.misc;

import com.sun.org.apache.xpath.internal.operations.Bool;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

import net.ccbluex.liquidbounce.event.EventTarget;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;



@ModuleInfo(name="CancelC03", description="NoC03Packets", category=ModuleCategory.MISC)
public class CancelC03 extends Module {
    private final BoolValue moveValue = new BoolValue("NoMove", false);

    @EventTarget
    public final void onPacket(PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(packet)) {
            event.cancelEvent();
        }
    }
    @EventTarget
    public final void onMove(MoveEvent event) {
        if(moveValue.get()){
            event.zero();
        }
    }
}
