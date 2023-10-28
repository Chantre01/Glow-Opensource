/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.client.*
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketConfirmTransaction
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

@ModuleInfo(name = "Blink2", description = "Blink", category = ModuleCategory.PLAYER)
class Bl1nk : Module() {
    private val packets = LinkedBlockingQueue<Packet<*>>()
    private var disableLogger = false
    private val pulseValue = BoolValue("Pulse", false)
    private val pulseDelayValue = IntegerValue("PulseDelay", 1000, 500, 5000)
    private val CancelC0f = BoolValue("CancelC0f", false)
    private val CancelS32 = BoolValue("CancelS32", false)
    private val CancelC0fResend = BoolValue("C0fResend", false)
    private val CancelAllCpacket = BoolValue("CancelAllClientPacket", false)
    private val CancelServerpacket = BoolValue("CancelServerPacket", false)
    private val inBus = LinkedList<Packet<INetHandlerPlayClient>>()
    var CanBlink: Boolean? = null
    private val pulseTimer = MSTimer()
    override fun onEnable() {
        if (mc.thePlayer == null) return
        pulseTimer.reset()
    }

    override fun onDisable() {
        if (mc.thePlayer == null) return
        blink()
    }
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if (mc.thePlayer == null || disableLogger) return
        if (packet is CPacketPlayer || CancelS32.get() && packet is SPacketConfirmTransaction) // Cancel all movement stuff
            event.cancelEvent()
        if (packet is Position || packet is PositionRotation ||
            packet is CPacketPlayerTryUseItemOnBlock ||
            packet is CPacketAnimation ||
            packet is CPacketEntityAction || packet is CPacketUseEntity || (packet::class.java.simpleName.startsWith("C", true) && CancelAllCpacket.get())
        ) {
            event.cancelEvent()
            packets.add(packet)
        }
        if (packet is CPacketConfirmTransaction && CancelC0f.get()) {
            event.cancelEvent()
            if (CancelC0fResend.get()) {
                packets.add(packet)
            }
        }
        if(packet::class.java!!.getSimpleName().startsWith("S", true) && CancelServerpacket.get())
        {
          if(packet is SPacketEntityVelocity && (mc.theWorld?.getEntityByID(packet.entityID) ?: return) == mc.thePlayer){return}
            event.cancelEvent()
            inBus.add(packet as Packet<INetHandlerPlayClient>)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (pulseValue.get() && pulseTimer.hasTimePassed(pulseDelayValue.get().toLong())) {
            blink()
            pulseTimer.reset()
        }
    }

    override val tag: String
        get() = packets.size.toString()

    private fun blink() {
        try {
            disableLogger = true
            while (!packets.isEmpty()) {
                mc2.connection!!.networkManager.sendPacket(packets.take())
            }
            while (!inBus.isEmpty()) {
                inBus.poll()?.processPacket(mc2!!.connection)
            }
            disableLogger = false
        } catch (e: Exception) {
            e.printStackTrace()
            disableLogger = false
        }
    }
}