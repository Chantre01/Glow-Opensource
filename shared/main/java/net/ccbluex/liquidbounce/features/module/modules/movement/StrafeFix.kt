package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.StrafeEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.util.math.MathHelper
import kotlin.math.abs

@ModuleInfo(name = "StrafeFix", category = ModuleCategory.MOVEMENT, description = "Maybe Useful lol")
class StrafeFix : Module() {

    private val silentFixVaule = BoolValue("Silent", true)

    var silentFix = false
    var doFix = false
    private var isOverwrited = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!isOverwrited) {
            silentFix = silentFixVaule.get()
            doFix = true
        }
    }

    override fun onDisable() {
        doFix = false
    }

    fun applyForceStrafe(isSilent: Boolean, runStrafeFix: Boolean) {
        silentFix = isSilent
        doFix = runStrafeFix
        isOverwrited = true
    }

    fun updateOverwrite() {
        isOverwrited = false
        doFix = state
        silentFix = silentFixVaule.get()
    }

    fun runStrafeFixLoop(isSilent: Boolean, event: StrafeEvent) {
        if (event.isCancelled) {
            return
        }
        val (yaw) = RotationUtils.targetRotation ?: return
        var strafe = event.strafe
        var forward = event.forward
        var friction = event.friction
        var factor = strafe * strafe + forward * forward

        val angleDiff =
            ((WMathHelper.wrapAngleTo180_float(mc.thePlayer!!.rotationYaw - yaw - 22.5f - 135.0f) + 180.0) / (45.0).toDouble()).toInt()
        val calcYaw = if (isSilent) {
            yaw + 45.0f * angleDiff.toFloat()
        } else yaw

        var calcMoveDir = abs(strafe).coerceAtLeast(abs(forward))
        calcMoveDir *= calcMoveDir
        var calcMultiplier = MathHelper.sqrt(calcMoveDir / Math.min(1.0f, calcMoveDir * 2.0f))

        if (isSilent) {
            when (angleDiff) {
                1, 3, 5, 7, 9 -> {
                    if ((abs(forward) > 0.005 || abs(strafe) > 0.005) && !(abs(forward) > 0.005 && abs(
                            strafe
                        ) > 0.005)
                    ) {
                        friction /= calcMultiplier
                    } else if (abs(forward) > 0.005 && abs(strafe) > 0.005) {
                        friction *= calcMultiplier
                    }
                }
            }
        }
        if (factor >= 1.0E-4F) {
            factor = MathHelper.sqrt(factor)

            if (factor < 1.0F) {
                factor = 1.0F
            }

            factor = friction / factor
            strafe *= factor
            forward *= factor

            val yawSin = MathHelper.sin((calcYaw * Math.PI / 180F).toFloat())
            val yawCos = MathHelper.cos((calcYaw * Math.PI / 180F).toFloat())

            mc.thePlayer!!.motionX += strafe * yawCos - forward * yawSin
            mc.thePlayer!!.motionZ += forward * yawCos + strafe * yawSin
        }
        event.cancelEvent()
    }
}
