/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */

package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.Rotation
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.CPacketEntityAction

@ModuleInfo(name = "Sprint", description = "Automatically sprints all the time.", category = ModuleCategory.MOVEMENT)
class Sprint : Module() {

    @JvmField
    val useItemValue = BoolValue("UseItem", false)

    @JvmField
    val useItemSwordValue = BoolValue("UseItemOnlySword", false)

    @JvmField
    val hungryValue = BoolValue("Hungry", true)

    @JvmField
    val sneakValue = BoolValue("Sneak", false)

    @JvmField
    val collideValue = BoolValue("Collide", false)

    @JvmField
    val jumpDirectionsValue = BoolValue("JumpDirections", false)

    @JvmField
    val allDirectionsValue = BoolValue("AllDirections", false)

    @JvmField
    val allDirectionsBypassValue = ListValue(
        "AllDirectionsBypass",
        arrayOf(
            "Rotate",
            "RotateSpoof",
            "Toggle",
            "Spoof",
            "SpamSprint",
            "NoStopSprint",
            "Minemora",
            "LimitSpeed",
            "None"
        ),
        "None"
    )
    private val allDirectionsLimitSpeedGround = BoolValue("AllDirectionsLimitSpeedOnlyGround", true)
    private val allDirectionsLimitSpeedValue = FloatValue(
        "AllDirectionsLimitSpeed",
        0.7f,
        0.5f,
        1f
    )
    private val noPacketValue = BoolValue("NoPackets", false)

    @JvmField
    var switchStat = false

    @JvmField
    var forceSprint = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (allDirectionsValue.get()) {
            when (allDirectionsBypassValue.get()) {
                "NoStopSprint" -> {
                    forceSprint = true
                }

                "SpamSprint" -> {
                    forceSprint = true
                    mc2.connection!!.sendPacket(
                        CPacketEntityAction(
                            mc2.player,
                            CPacketEntityAction.Action.START_SPRINTING
                        )
                    )
                }

                "Spoof" -> {
                    mc2.connection!!.sendPacket(
                        CPacketEntityAction(
                            mc2.player,
                            CPacketEntityAction.Action.START_SPRINTING
                        )
                    )
                    switchStat = true
                }
            }
            if (RotationUtils.getRotationDifference(
                    Rotation(MovementUtils.movingYaw, mc.thePlayer!!.rotationPitch),
                    Rotation(mc.thePlayer!!.rotationYaw, mc.thePlayer!!.rotationPitch)
                ) > 30
            ) {
                when (allDirectionsBypassValue.get()) {
                    "Rotate" -> RotationUtils.setTargetRotation(
                        Rotation(
                            MovementUtils.movingYaw,
                            mc.thePlayer!!.rotationPitch
                        ), 2
                    )

                    "RotateSpoof" -> {
                        switchStat = !switchStat
                        if (switchStat) {
                            RotationUtils.setTargetRotation(
                                Rotation(
                                    MovementUtils.movingYaw,
                                    mc.thePlayer!!.rotationPitch
                                )
                            )
                        }
                    }

                    "Toggle" -> {
                        mc2.connection!!.sendPacket(
                            CPacketEntityAction(
                                mc2.player,
                                CPacketEntityAction.Action.STOP_SPRINTING
                            )
                        )
                        mc2.connection!!.sendPacket(
                            CPacketEntityAction(
                                mc2.player,
                                CPacketEntityAction.Action.START_SPRINTING
                            )
                        )
                    }

                    "Minemora" -> {
                        if (mc.thePlayer!!.onGround && RotationUtils.getRotationDifference(
                                Rotation(
                                    MovementUtils.movingYaw,
                                    mc.thePlayer!!.rotationPitch
                                )
                            ) > 60
                        ) {
                            mc.thePlayer!!.setPosition(
                                mc.thePlayer!!.posX,
                                mc.thePlayer!!.posY + 0.0000013,
                                mc.thePlayer!!.posZ
                            )
                            mc.thePlayer!!.motionY = 0.0
                        }
                    }

                    "LimitSpeed" -> {
                        if (!allDirectionsLimitSpeedGround.get() || mc.thePlayer!!.onGround) {
                            MovementUtils.limitSpeedByPercent(allDirectionsLimitSpeedValue.get())
                        }
                    }
                }
            }
        } else {
            switchStat = false
            forceSprint = false
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is CPacketEntityAction) {
            if (allDirectionsValue.get()) {
                when (allDirectionsBypassValue.get()) {
                    "SpamSprint", "NoStopSprint" -> {
                        if (packet.action == CPacketEntityAction.Action.STOP_SPRINTING) {
                            event.cancelEvent()
                        }
                    }

                    "Toggle" -> {
                        if (switchStat) {
                            if (packet.action == CPacketEntityAction.Action.STOP_SPRINTING) {
                                event.cancelEvent()
                            } else {
                                switchStat = !switchStat
                            }
                        } else {
                            if (packet.action == CPacketEntityAction.Action.START_SPRINTING) {
                                event.cancelEvent()
                            } else {
                                switchStat = !switchStat
                            }
                        }
                    }

                    "Spoof" -> {
                        if (switchStat) {
                            if (packet.action == CPacketEntityAction.Action.STOP_SPRINTING || packet.action == CPacketEntityAction.Action.START_SPRINTING) {
                                event.cancelEvent()
                            }
                        }
                    }
                }
            }
            if (noPacketValue.get() && !event.isCancelled) {
                if (packet.action == CPacketEntityAction.Action.STOP_SPRINTING || packet.action == CPacketEntityAction.Action.START_SPRINTING) {
                    event.cancelEvent()
                }
            }
        }
    }

}
