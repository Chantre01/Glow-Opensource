/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.AnimationUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ColorValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import java.awt.Color

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER, array = false)
class HUD : Module() {
    private val toggleMessageModeValue =
        ListValue("ToggleMessageMode", arrayOf("None", "Chat", "Notifications"), "Notifications")
    private val toggleSoundValue = ListValue(
        "ToggleSound",
        arrayOf("None", "Default", "Custom", "Sigma", "Sinka", "Fallen", "Pride", "LB+"),
        "Custom"
    )
    companion object {

        @JvmField
        var redValue = IntegerValue("Red", 0, 0, 255)
        @JvmField
        var greenValue = IntegerValue("Green", 0, 0, 255)
        @JvmField
        var blueValue = IntegerValue("Blue", 0, 0, 255)
        @JvmField
        var alphaValue = IntegerValue("Alpha", 0, 0, 255)

    }
    val customHotbarValue = BoolValue("CustomHotbar", true)
    val animHotbarValue = BoolValue("AnimatedHotbar", true)
    val inventoryParticle = BoolValue("InventoryParticle", false)
    private val blurValue = BoolValue("Blur", false)
    val fontChatValue = BoolValue("FontChat", false)
    val chatRect = BoolValue("ChatRect", false)
    val chatAnimValue = BoolValue("ChatAnimation", true)
    private var hotBarX = 0F

    @EventTarget
    fun onRender2D(event: Render2DEvent?) {
        if (classProvider.isGuiHudDesigner(mc.currentScreen))
            return

        LiquidBounce.hud.render(false)
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        LiquidBounce.hud.update()
    }

    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }
    @EventTarget(ignoreCondition = true)
    fun onTick(event: TickEvent) {
        LiquidBounce.moduleManager.toggleMessageMode = toggleMessageModeValue.values.indexOf(toggleMessageModeValue.get())
        LiquidBounce.moduleManager.toggleSoundMode = toggleSoundValue.values.indexOf(toggleSoundValue.get())
    }


    @EventTarget(ignoreCondition = true)
    fun onScreen(event: ScreenEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) return
        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.guiScreen != null &&
            !(classProvider.isGuiChat(event.guiScreen) || classProvider.isGuiHudDesigner(event.guiScreen))) mc.entityRenderer.loadShader(classProvider.createResourceLocation("liquidbounce" + "/blur.json")) else if (mc.entityRenderer.shaderGroup != null &&
            mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("liquidbounce/blur.json")) mc.entityRenderer.stopUseShader()
    }
    fun getAnimPos(pos: Float): Float {
        if (state && animHotbarValue.get()) hotBarX =
            AnimationUtils.animate(pos.toDouble(), hotBarX.toDouble(),
                (0.02F * RenderUtils.deltaTime.toFloat()).toDouble()
            ).toFloat()
        else hotBarX = pos

        return hotBarX
    }

    init {
        state = true
    }
}