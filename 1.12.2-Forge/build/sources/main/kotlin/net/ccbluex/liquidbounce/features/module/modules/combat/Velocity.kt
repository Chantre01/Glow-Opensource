/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.server.SPacketEntityVelocity
import kotlin.math.cos
import kotlin.math.sin

@ModuleInfo(name = "Velocity", description = "Allows you to modify the amount of knockback you take.", category = ModuleCategory.COMBAT)
class Velocity : Module() {

    /**
     * OPTIONS
     */
    private val horizontalValue = FloatValue("Horizontal", 0F, 0F, 1F)
    private val verticalValue = FloatValue("Vertical", 0F, 0F, 1F)
    private val modeValue = ListValue("Mode", arrayOf("Simple",
        "HytCancel",
        "AAC",
        "AACPush",
        "AACZero",
        "Reverse",
        "SmoothReverse",
        "Jump",
        "Glitch",
        "AAC4",
        "GrimAC",
        "AntiCheat",
        "Noxz",
        "Hyt",
        "GrimFull",
        "JumpPlus",
        "GrimReduce"
    ), "Simple")

    // Reverse
    private val reverseStrengthValue = FloatValue("ReverseStrength", 1F, 0.1F, 1F)
    private val reverse2StrengthValue = FloatValue("SmoothReverseStrength", 0.05F, 0.02F, 0.1F)

    // AAC Push
    private val aacPushXZReducerValue = FloatValue("AACPushXZReducer", 2F, 1F, 3F)
    private val aacPushYReducerValue = BoolValue("AACPushYReducer", true)

    private val onlyCombatValue = BoolValue("OnlyCombat", false)
    private val onlyGroundValue = BoolValue("OnlyGround", false)
    private val onlyMoveValue = BoolValue("OnlyMove", false)
    /**
     * VALUES
     */
    private var velocityTimer = MSTimer()
    private var velocityInput = false

    // SmoothReverse
    private var reverseHurt = false

    // AACPush
    private var jump = false
    private var canCancelJump = false

    override val tag: String
        get() = modeValue.get()

    override fun onDisable() {
        mc.thePlayer?.speedInAir = 0.02F
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.isInWater || thePlayer.isInLava || thePlayer.isInWeb)
            return
        if ((onlyGroundValue.get() && !mc.thePlayer!!.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
            return
        }
        when (modeValue.get().toLowerCase()) {
            "grimreduce"->{
                if (thePlayer.hurtTime > 0){
                    thePlayer.motionX += -1.0E-7
                    thePlayer.motionY += -1.0E-7
                    thePlayer.motionZ += -1.0E-7
                    thePlayer.isAirBorne = true
                }
            }
            "aac4"->{
                if (!thePlayer.onGround) {
                    if (velocityInput) {
                        thePlayer.speedInAir = 0.02f
                        thePlayer.motionX *= 0.6
                        thePlayer.motionZ *= 0.6
                    }
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false
                    thePlayer.speedInAir = 0.02f
                }
            }
            "anticheat"->{
                if(thePlayer.hurtTime > 0){
                    thePlayer.motionX += -1.1E-10
                    thePlayer.motionY += -1.1E-10
                    thePlayer.motionZ += -1.1E-10
                    thePlayer.isAirBorne = true
                }
                if(thePlayer.hurtTime >= 3){
                    thePlayer.motionX += -0.1
                    thePlayer.motionY += -0.1
                    thePlayer.motionZ += -0.1
                    thePlayer.isAirBorne = true
                    thePlayer.speedInAir = 0.01F
                }

            }
            "grimac" -> {
                if ( thePlayer.hurtTime > 0) {
                    thePlayer.motionX += -1.1E-7
                    thePlayer.motionY += -1.1E-7
                    thePlayer.motionZ += -1.2E-7
                    thePlayer.isAirBorne = true
                }
            }
            "jump" -> if (thePlayer.hurtTime > 0 && thePlayer.onGround) {
                thePlayer.motionY = 0.42

                val yaw = thePlayer.rotationYaw * 0.017453292F

                thePlayer.motionX -= sin(yaw) * 0.2
                thePlayer.motionZ += cos(yaw) * 0.2
            }

            "glitch" -> {
                thePlayer.noClip = velocityInput

                if (thePlayer.hurtTime == 7)
                    thePlayer.motionY = 0.4

                velocityInput = false
            }

            "reverse" -> {
                if (!velocityInput)
                    return

                if (!thePlayer.onGround) {
                    MovementUtils.strafe(MovementUtils.speed * reverseStrengthValue.get())
                } else if (velocityTimer.hasTimePassed(80L))
                    velocityInput = false
            }

            "aac4" -> {
                if (!thePlayer.onGround) {
                    if (velocityInput) {
                        thePlayer.speedInAir = 0.02f
                        thePlayer.motionX *= 0.6
                        thePlayer.motionZ *= 0.6
                    }
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false
                    thePlayer.speedInAir = 0.02f
                }
            }

            "smoothreverse" -> {
                if (!velocityInput) {
                    thePlayer.speedInAir = 0.02F
                    return
                }

                if (thePlayer.hurtTime > 0)
                    reverseHurt = true

                if (!thePlayer.onGround) {
                    if (reverseHurt)
                        thePlayer.speedInAir = reverse2StrengthValue.get()
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false
                    reverseHurt = false
                }
            }

            "aac" -> if (velocityInput && velocityTimer.hasTimePassed(80L)) {
                thePlayer.motionX *= horizontalValue.get()
                thePlayer.motionZ *= horizontalValue.get()
                //mc.thePlayer.motionY *= verticalValue.get() ?
                velocityInput = false
            }

            "aacpush" -> {
                if (jump) {
                    if (thePlayer.onGround)
                        jump = false
                } else {
                    // Strafe
                    if (thePlayer.hurtTime > 0 && thePlayer.motionX != 0.0 && thePlayer.motionZ != 0.0)
                        thePlayer.onGround = true

                    // Reduce Y
                    if (thePlayer.hurtResistantTime > 0 && aacPushYReducerValue.get()
                        && !LiquidBounce.moduleManager[Speed::class.java]!!.state)
                        thePlayer.motionY -= 0.014999993
                }

                // Reduce XZ
                if (thePlayer.hurtResistantTime >= 19) {
                    val reduce = aacPushXZReducerValue.get()

                    thePlayer.motionX /= reduce
                    thePlayer.motionZ /= reduce
                }
            }

            "aaczero" -> if (thePlayer.hurtTime > 0) {
                if (!velocityInput || thePlayer.onGround || thePlayer.fallDistance > 2F)
                    return

                thePlayer.motionY -= 1.0
                thePlayer.isAirBorne = true
                thePlayer.onGround = true
            } else
                velocityInput = false
        }
    }
    var hytCount = 24
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val thePlayer = mc.thePlayer ?: return

        val packet = event.packet
        if ((onlyGroundValue.get() && !mc.thePlayer!!.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
            return
        }

        when (modeValue.get().toLowerCase()) {
            "hytcancel"->{
                if(packet.unwrap() is CPacketConfirmTransaction) event.cancelEvent()
            }
        }
        if (classProvider.isSPacketEntityVelocity(packet)) {
            val packetEntityVelocity = packet.asSPacketEntityVelocity()


            if ((mc.theWorld?.getEntityByID(packetEntityVelocity.entityID) ?: return) != thePlayer)
                return

            velocityTimer.reset()

            when (modeValue.get().toLowerCase()) {
                "hytcancel"->{
                    event.cancelEvent()
                }
                "jumpplus"->{
                    if(packet.unwrap() is SPacketEntityVelocity) {
//                    jumpingflag = true

                        if(mc.thePlayer!!.hurtTime != 0) {
                            event.cancelEvent()
                            packet.asSPacketEntityVelocity().motionX = 0
                            packet.asSPacketEntityVelocity().motionY = 0
                            packet.asSPacketEntityVelocity().motionZ = 0
                        }
                    }
                }
                "hyt"->{
                    if (thePlayer.onGround) {
                        velocityInput = true
                        val yaw = thePlayer.rotationYaw * 0.017453292F
                        packetEntityVelocity.motionX = (packetEntityVelocity.motionX * 0.75).toInt()
                        packetEntityVelocity.motionZ = (packetEntityVelocity.motionZ * 0.75).toInt()
                        thePlayer.motionX -= sin(yaw) * 0.2
                        thePlayer.motionZ += cos(yaw) * 0.2
                    }
                }
                "grimfull"->{
                    if (thePlayer.onGround) {
                        canCancelJump = false
                        packetEntityVelocity.motionX = (0.985114).toInt()
                        packetEntityVelocity.motionY = (0.885113).toInt()
                        packetEntityVelocity.motionZ = (0.785112).toInt()
                        thePlayer.motionX /= 1.75
                        thePlayer.motionZ /= 1.75
                    }
                }
                "simple" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == 0F && vertical == 0F)
                        event.cancelEvent()

                    packetEntityVelocity.motionX = (packetEntityVelocity.motionX * horizontal).toInt()
                    packetEntityVelocity.motionY = (packetEntityVelocity.motionY * vertical).toInt()
                    packetEntityVelocity.motionZ = (packetEntityVelocity.motionZ * horizontal).toInt()
                }
                "noxz" -> {
                    if (packetEntityVelocity.motionX == 0 && packetEntityVelocity.motionZ == 0) {
                        return
                    }
                    val ka = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
                    val target = LiquidBounce.combatManager.getNearByEntity(ka.rangeValue.get() + 1) ?: return
                    mc.thePlayer!!.motionX = 0.0
                    mc.thePlayer!!.motionZ = 0.0
                    packetEntityVelocity.motionX = 0
                    packetEntityVelocity.motionZ = 0
                    for (i in 0..hytCount) {
                        mc.thePlayer!!.sendQueue.addToSendQueue(classProvider.createCPacketUseEntity(target,ICPacketUseEntity.WAction.ATTACK))
                        mc.thePlayer!!.sendQueue.addToSendQueue(classProvider.createCPacketAnimation())
                    }
                    if (hytCount > 12) hytCount -= 5
                }
                "aac", "aac4", "reverse", "smoothreverse", "aaczero" -> velocityInput = true

                "glitch" -> {
                    if (!thePlayer.onGround)
                        return

                    velocityInput = true
                    event.cancelEvent()
                }
            }
        }
    }

    @EventTarget
    fun onJump(event: JumpEvent) {
        val thePlayer = mc.thePlayer

        if (thePlayer == null || thePlayer.isInWater || thePlayer.isInLava || thePlayer.isInWeb)
            return
        if ((onlyGroundValue.get() && !mc.thePlayer!!.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat) || (onlyMoveValue.get() && !MovementUtils.isMoving)) {
            return
        }
        when (modeValue.get().toLowerCase()) {

            "aac4" -> {
                if (thePlayer.hurtTime > 0) {
                    event.cancelEvent()
                    velocityInput = false
                }
            }
            "aacpush" -> {
                jump = true

                if (!thePlayer.isCollidedVertically)
                    event.cancelEvent()
            }
            "aac4" -> {
                if (thePlayer.hurtTime > 0) {
                    event.cancelEvent()
                }
            }
            "aaczero" -> if (thePlayer.hurtTime > 0)
                event.cancelEvent()
        }
    }
}
