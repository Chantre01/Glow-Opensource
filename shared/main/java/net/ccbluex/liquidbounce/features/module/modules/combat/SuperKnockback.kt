package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(
    name = "SuperKnockback",
    description = "Increases knockback dealt to other entities.",
    category = ModuleCategory.COMBAT
)
class SuperKnockback : Module() {

    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (classProvider.isEntityLivingBase(event.targetEntity)) {
            if (event.targetEntity!!.asEntityLivingBase().hurtTime > hurtTimeValue.get())
                return

            val player = mc.thePlayer ?: return
            player.sprinting = true
            mc.netHandler.addToSendQueue(
                classProvider.createCPacketEntityAction(
                    player,
                    ICPacketEntityAction.WAction.STOP_SPRINTING
                )
            )
            mc.netHandler.addToSendQueue(
                classProvider.createCPacketEntityAction(
                    player,
                    ICPacketEntityAction.WAction.START_SPRINTING
                )
            )
            mc.netHandler.addToSendQueue(
                classProvider.createCPacketEntityAction(
                    player,
                    ICPacketEntityAction.WAction.STOP_SPRINTING
                )
            )
            mc.netHandler.addToSendQueue(
                classProvider.createCPacketEntityAction(
                    player,
                    ICPacketEntityAction.WAction.START_SPRINTING
                )
            )
            player.serverSprintState = true
        }
    }

}/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/

package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.world.Timer
import net.ccbluex.liquidbounce.utils.MovementUtils.isMoving

import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraft.network.play.client.CPacketEntityAction.Action.*
import org.lwjgl.input.Keyboard

@ModuleInfo(name = "SuperKnockBack", description = "More Knockback",
    category = ModuleCategory.COMBAT)
class SuperKnockback : Module() {

    private val delay = IntegerValue("Delay", 0, 0, 500)
    private val hurtTime = IntegerValue("HurtTime", 10, 0, 10)
    private val mode = ListValue("Mode", arrayOf("Legit", "Old", "Silent", "Packet", "SneakPacket"), "Old")
    private val onlyGround = BoolValue("OnlyGround", false)

    private val onlyMove = BoolValue("OnlyMove", true)
    private val onlyMoveForward = BoolValue("OnlyMoveForward", true)

    private var ticks = 0

    private val timer = MSTimer()


    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity !is EntityLivingBase)
            return

        if (event.targetEntity.hurtTime > hurtTime.get() || !timer.hasTimePassed(delay.get().toLong()) || (onlyGround.get() && !mc.thePlayer!!.onGround))
            return

        if (onlyMove.get() && (!isMoving || (onlyMoveForward.get() && mc.thePlayer!!.movementInput.moveStrafe != 0f)))
            return

        when (mode.get()) {
            "Old" -> {
                // Users reported that this mode is better than the other ones

                if (mc2.player.isSprinting) {
                    classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING)
                }
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING)
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING)
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING)

                mc2.player.isSprinting = true
                mc.thePlayer!!.serverSprintState = true
            }

            "Legit", "Silent" -> ticks = 2

            "Packet" -> {
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING)
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING)
            }

            "SneakPacket" -> {
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING)
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING)
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING)
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING)

            }
        }

        timer.reset()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mode.get() == "Legit") {
            if (ticks == 2) {
                mc2.player.isSprinting = false
                ticks--
            } else if (ticks == 1) {
                mc2.player.isSprinting = true
                ticks--
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is CPacketPlayer && mode.get() == "Silent") {
            if (ticks == 2) {
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SPRINTING)
                ticks--
            } else if (ticks == 1) {
                classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SPRINTING)
                ticks--
            }
        }
    }

    override val tag: String?
        get() = mode.get()
}

 */