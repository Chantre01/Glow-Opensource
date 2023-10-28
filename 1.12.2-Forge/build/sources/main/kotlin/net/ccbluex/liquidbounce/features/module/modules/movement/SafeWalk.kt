/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.StrafeEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.client.settings.GameSettings

@ModuleInfo(name = "SafeWalk", description = "Prevents you from falling down as if you were sneaking.", category = ModuleCategory.MOVEMENT)
class SafeWalk : Module() {
    private val Value = FloatValue("Value",0.01f,0.01f,0.2f)
    private val AirSafe = BoolValue("AirSafe",false)
    var safeWalking = false
    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if(mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer!!, mc.thePlayer!!.entityBoundingBox
                .offset(0.0, -0.5, 0.0).expand(-Value.get().toDouble(), 0.0, -Value.get().toDouble())).isEmpty()||(AirSafe.get() && !mc.thePlayer!!.onGround)) {
            mc.gameSettings.keyBindForward.pressed = false
            mc.gameSettings.keyBindBack.pressed = false
            mc.gameSettings.keyBindRight.pressed = false
            mc.gameSettings.keyBindLeft.pressed = false
            safeWalking = true
        }else{
            safeWalking = false
            mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc2.gameSettings.keyBindForward)
            mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc2.gameSettings.keyBindBack)
            mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc2.gameSettings.keyBindRight)
            mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc2.gameSettings.keyBindLeft)
        }
    }
    @EventTarget
    fun onStrafe(event: StrafeEvent){
        if(mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer!!, mc.thePlayer!!.entityBoundingBox
                .offset(0.0, -0.5, 0.0).expand(-Value.get().toDouble(), 0.0, -Value.get().toDouble())).isEmpty()||(AirSafe.get() && !mc.thePlayer!!.onGround)) {
            mc.gameSettings.keyBindForward.pressed = false
            mc.gameSettings.keyBindBack.pressed = false
            mc.gameSettings.keyBindRight.pressed = false
            mc.gameSettings.keyBindLeft.pressed = false
            event.cancelEvent()
        }else{
            mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc2.gameSettings.keyBindForward)
            mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc2.gameSettings.keyBindBack)
            mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc2.gameSettings.keyBindRight)
            mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc2.gameSettings.keyBindLeft)
        }
    }

}
