package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "ScaffoldHelper", description = "ScaffoldHelper", category = ModuleCategory.WORLD)
class ScaffoldHelper : Module() {
    var scaffoldmodule = LiquidBounce.moduleManager.getModule(Scaffold::class.java) as Scaffold
    override fun onDisable() {
        scaffoldmodule.state = false
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        scaffoldmodule.state = !mc.thePlayer!!.onGround
    }
}