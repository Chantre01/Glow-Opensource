package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.enums.BlockType
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.network.play.server.SPacketPlayerPosLook

@ModuleInfo(name = "EntitySpeed", description = "from kid", category = ModuleCategory.MOVEMENT)
class EntitySpeed : Module() {
    private val onlyAir = BoolValue("OnlyAir",false)
    private val okstrafe = BoolValue("Strafe",false)
    private val keepSprint = BoolValue("KeepSprint",false)
    private val speedUp = BoolValue("SpeedUp",false)
    private val speedplus = BoolValue("Speedplus",true)
    private val speedplusvaule = IntegerValue("Speedplusvaule", 0, 0, 10)
    private val speedd = IntegerValue("Speed", 0, 0, 10)
    private var speed = 0
    private val distance = FloatValue("speedRange", 0f, 0f, 3f)
    private val distance2 = FloatValue("starfeRange", 0f, 0f, 3f)
    private var speeded = false
    private var f = 0
    var strgo = false
    private var pre = false
    var sprint = false

    override fun onEnable() {
        speeded = false
    }
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if (packet is SPacketPlayerPosLook){
            f = 10
        }
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        speed = speedd.get()
        f--
        if (f >= 0) {
            mc.thePlayer!!.motionX *= 1
            mc.gameSettings.keyBindForward.pressed = false
            mc.gameSettings.keyBindLeft.pressed = false
            mc.gameSettings.keyBindBack.pressed = false
            mc.gameSettings.keyBindRight.pressed = false
            mc.gameSettings.keyBindJump.pressed = false
            return
        }
        for (entity in mc.theWorld!!.loadedEntityList) {
            if (entity.unwrap() is EntityLivingBase && entity.entityId != mc.thePlayer!!.entityId && mc.thePlayer!!.getDistanceToEntityBox(
                    entity
                ) <= distance.get() && (!onlyAir.get() || !mc.thePlayer!!.onGround)
            ) {
                if(speedplus.get()){
                    speed = (speedd.get()+speedplusvaule.get()*(1- mc.thePlayer!!.getDistanceToEntityBox(entity)/distance.get())).toInt()
                }
                if (speedUp.get()) {
                    mc.thePlayer!!.motionX *= (1 + (speed * 0.01))
                    mc.thePlayer!!.motionZ *= (1 + (speed * 0.01))
                }
                if (keepSprint.get()) {
                    sprint = true
                }
                if (okstrafe.get() && mc.thePlayer!!.getDistanceToEntityBox(
                        entity
                    ) <= distance2.get()) {
                    strgo = true
                }
                return
            }
            sprint = false
            strgo = false
        }
    }


}