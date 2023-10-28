/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import cn.utils.EaseUtils
import cn.utils.miku.render.round.RoundedUtil
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", single = true)
class Notifications(x: Double = 3.0, y: Double = 11.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    private val exampleNotification = Notification("Notification", "This is an example notification.", NotifyType.INFO)

    /**
     * Draw element
     */

    override fun drawElement(): Border? {
        // bypass java.util.ConcurrentModificationException
        LiquidBounce.hud.notifications.map { it }.forEachIndexed { index, notify ->
            GL11.glPushMatrix()
            val font = Fonts.font40

            if(notify.drawNotification(index,font)){
                LiquidBounce.hud.notifications.remove(notify)
            }

            GL11.glPopMatrix()
        }
        val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
        if (classProvider.isGuiHudDesigner((mc.currentScreen))) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification))
                LiquidBounce.hud.addNotification(exampleNotification)

            exampleNotification.fadeState = FadeState.STAY
            exampleNotification.displayTime = System.currentTimeMillis()
//            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-exampleNotification.width.toFloat(), -exampleNotification.height.toFloat(),0F,0F)
        }

        return null
    }

}

class Notification(val title: String, val content: String, val type: NotifyType, val time: Int=1500, val animeTime: Int=500) {
    var width = 100.coerceAtLeast(Fonts.prod35.getStringWidth(content) + 22)


    var height = 45
    var fadeState = FadeState.IN
    var x = 0f
    private val widthSpacing = 25f
    var y = 0f
    val height2 = 28

    var nowY = -height
    var displayTime = System.currentTimeMillis()
    var animeXTime = System.currentTimeMillis()
    var animeYTime = System.currentTimeMillis()
    var notifWidth =
        17 + Math.max(Fonts.prod35.getStringWidth(content), Fonts.prod35.getStringWidth(title)) + widthSpacing

    var notifX: Float = (notifWidth + 5)

    /**
     * Draw notification
     */

    fun drawNotification(index: Int, font: IFontRenderer): Boolean {

        val realY = -(index + 1) * height
        val nowTime = System.currentTimeMillis()

        //Y-Axis Animation
        if (nowY != realY) {
            var pct = (nowTime - animeYTime) / animeTime.toDouble()
            if (pct > 1) {
                nowY = realY
                pct = 1.0
            } else {
                pct = EaseUtils.easeOutExpo(pct)
            }
            GL11.glTranslated(0.0, (realY - nowY) * pct, 0.0)
        } else {
            animeYTime = nowTime
        }
        GL11.glTranslated(0.0, nowY.toDouble(), 0.0)

        //X-Axis Animation
        var pct = (nowTime - animeXTime) / animeTime.toDouble()
        when (fadeState) {
            FadeState.IN -> {
                if (pct > 1) {
                    fadeState = FadeState.STAY
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = EaseUtils.easeOutExpo(pct)
            }

            FadeState.STAY -> {
                pct = 1.0
                if ((nowTime - animeXTime) > time) {
                    fadeState = FadeState.OUT
                    animeXTime = nowTime
                }
            }

            FadeState.OUT -> {
                if (pct > 1) {
                    fadeState = FadeState.END
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = 1 - EaseUtils.easeInExpo(pct)
            }

            FadeState.END -> {
                return true
            }
        }
        GL11.glTranslated(width - (width * pct), 0.0, 0.0)
        GL11.glTranslatef(-width.toFloat(), 0F, 0F)
        when (UIEffects.hudstyle.get()) {
            "CustomColor","CustomShader","CustomOutlined" -> {
                UIEffects.render(0.0f, -4.0f, 143f, 27f, 10f)
                colorText()
            }
            "OneTapV2"->{
                RoundedUtil.drawRound(0.0f, -4.0f, 143f, 27f, 1f, Color(28, 30, 40))
                RoundedUtil.drawRound(-0.5f, -4.0f - 1.7f, 144f, 1.2f, 0.5f, Color(UIEffects.Hudcolor.getValue()))
                colorText()
            }
        }

        GlStateManager.resetColor()
        return false
    }
    private fun colorText() {
        //text
        Fonts.SimpleNotifications.drawString(
            if (type === NotifyType.SUCCESS || type === NotifyType.INFO) "f" else "g",
            6.0f,
            10.5f,
            Color(255, 255, 255).rgb
        )
        cn.novoline.impl.Fonts.SFTHIN.SFTHIN_17.SFTHIN_17.drawString("Modules", 4, 0.5f.toInt(), Color(255, 255, 255).rgb)
        cn.novoline.impl.Fonts.SFTHIN.SFTHIN_17.SFTHIN_17.drawString(
            content,
            6 + Fonts.SimpleNotifications.getStringWidth("g") + 4,
            10.5f.toInt(),
            Color(255, 255, 255, 255).rgb);
    }

}
enum class NotifyType() {
    SUCCESS,
    ERROR,
    WARNING,
    INFO;
    }
enum class FadeState { IN, STAY, OUT, END }


