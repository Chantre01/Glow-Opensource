package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.TickEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.MinecraftImpl
import net.ccbluex.liquidbounce.injection.backend.PacketImpl
import net.ccbluex.liquidbounce.injection.backend.WorldClientImpl
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion
import net.minecraft.network.play.server.SPacketPlayerPosLook
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

/**
 * Skid By GaoWenBo
 * Kt By Newno
 * @author 7078z
 * @Data 2023.9.26
 *
 * 泛滥狗 1670007933 替换class大神 也是一条缺钱的恶狗
 * 改description 的死了亲娘了
 */
@ModuleInfo(name = "GrimVelocity", description = "7078z", category = ModuleCategory.COMBAT)
class GrimVelo : Module() {
    private val sendC03Value = BoolValue("SendC03", true)
    private val breakValue = BoolValue("BreakBlock", true)
    private val alwayValue = BoolValue("Alway", false)
    private var gotVelo = false
    private var lastWasTeleport = false
    override fun onEnable() {
        gotVelo = false
        lastWasTeleport = false
    }

    @EventTarget
    fun onTick(event: TickEvent?) {
        val thePlayer = mc.thePlayer ?: return
        val theWorld = mc.theWorld ?: return
        val timer = (mc as MinecraftImpl).wrapped.timer ?: return
        if (alwayValue.get() || gotVelo) {
            val connection = mc.wrapped.connection ?: return
            gotVelo = false
            if (sendC03Value.get()) {
                connection.sendPacket(CPacketPlayer(thePlayer.onGround))
                try {
                    val f = timer.javaClass.getDeclaredField("field_74277_g")
                    f.isAccessible = true
                    val t = f[timer] as Long
                    f[timer] = t + 50L
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            val pos = BlockPos(thePlayer.posX, thePlayer.posY + 1.0, thePlayer.posZ)
            connection.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    pos,
                    EnumFacing.DOWN
                )
            )
            if (breakValue.get()) {
                (theWorld as WorldClientImpl).wrapped.setBlockToAir(pos)
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val thePlayer = mc.thePlayer ?: return
        val packet = (event.packet as PacketImpl<*>).wrapped
        if (packet is SPacketPlayerPosLook) {
            lastWasTeleport = true
        } else if (!lastWasTeleport && packet is SPacketEntityVelocity) {
            if (packet.entityID == thePlayer.entityId) {
                event.cancelEvent()
                gotVelo = true
            }
        } else if (packet is SPacketExplosion) {
            if (packet.motionX != 0f || packet.motionY != 0f || packet.motionZ != 0f) {
                event.cancelEvent()
                gotVelo = true
            }
        } else if (packet.javaClass.name.startsWith("net.minecraft.network.play.server.SPacket")) {
            lastWasTeleport = false
        }
    }
    override val tag: String
        get() = "Grim"
}
