/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.play.server.SPacketDisconnect

@ModuleInfo(name = "Parkour", description = "Automatically jumps when reaching the edge of a block.", category = ModuleCategory.MOVEMENT)
class Parkour : Module() {
    val canjump = BoolValue("noSelfJump",false)
    @EventTarget
    fun onPacket(event: PacketEvent) {

        val packet = event.packet
        if(packet is SPacketDisconnect){
            state = false
        }
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if(canjump.get()){
            if (MovementUtils.isMoving && mc.thePlayer!!.onGround && !mc2.player.isSneaking && !mc.gameSettings.keyBindSneak.isKeyDown  &&
                mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer!!, mc.thePlayer!!.entityBoundingBox
                    .offset(0.0, -0.5, 0.0).expand(-0.03, 0.0, -0.03)).isEmpty()) {
                mc.gameSettings.keyBindJump.pressed = true
            }else{
                mc.gameSettings.keyBindJump.pressed = false
            }
        }
    }
    @EventTarget
    fun onJump(event: JumpEvent){
        if (MovementUtils.isMoving && mc.thePlayer!!.onGround && !mc2.player.isSneaking && !mc.gameSettings.keyBindSneak.isKeyDown &&
            mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer!!, mc.thePlayer!!.entityBoundingBox
                .offset(0.0, -0.5, 0.0).expand(-0.05, 0.0, -0.05)).isEmpty()) {
            return
        }else{
            event.cancelEvent()
        }
    }
}
