package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

/* compiled from: C07Velocity.kt */
@ModuleInfo(name = "C07Velocity", description = "Allows you to modify the amount of knockback you take.", category = ModuleCategory.COMBAT)
/* loaded from: C07Velocity.class */
public final class C07Velocity extends Module {
    @NotNull
    private final IntegerValue ticks = new IntegerValue("Ticks", 3, 0, 10);
    private int tick;

    @EventTarget
    public void onPacket(PacketEvent event) {

        IPacket packet = event.getPacket();
        if ((packet instanceof SPacketEntityVelocity) && ((SPacketEntityVelocity) packet).getEntityID() == mc2.player.getEntityId()) {
            event.cancelEvent();
            this.tick = ((Number) this.ticks.get()).intValue();
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {

        if (this.tick > 0) {
            this.tick--;
            mc2.world.setBlockToAir(new BlockPos(mc2.player.posX, mc2.player.posY, mc2.player.posZ));
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, new WBlockPos(mc2.player.posX, mc2.player.posY, mc2.player.posZ), classProvider.getEnumFacing(EnumFacingType.UP)));
        }
    }
}