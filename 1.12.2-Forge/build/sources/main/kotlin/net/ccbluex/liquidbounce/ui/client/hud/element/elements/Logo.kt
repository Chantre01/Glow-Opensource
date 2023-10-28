package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import cn.utils.miku.render.round.RoundedUtil
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects
import net.ccbluex.liquidbounce.feng.FontLoaders
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import java.awt.Color

@ElementInfo(name = "LogoTitle")
class Logo(x: Double = -8.0, y: Double = 57.0, scale: Float = 1F) : Element(x, y, scale) {

    override fun drawElement(): Border? {
        val username: String = mc.session.username
        val ping = EntityUtils.getPing(mc.thePlayer!!).toString()
        val fps = Minecraft.getDebugFPS().toString() + "fps"
        //draw Background
        //if (UIEffects.hudblur.get()) {
        //    UIEffects.renderblur(renderX,renderY, Fonts.sfbold45.getStringWidth("GlowClient") + Fonts.fontSFUI45.getStringWidth(
        //        " | $username | $ping | $fps"
        //    ) + 35f, 16f, 5f
        //    )
        //}

        when (UIEffects.hudstyle.get()) {
            "CustomColor","CustomShader","CustomOutlined" -> {
                UIEffects.render(0f, 0f, Fonts.sfbold45.getStringWidth("GlowClient") + Fonts.fontSFUI45.getStringWidth(
                    " | $username | $ping | $fps"
                ) + 35f, 16f, 10f)
                colorText()
            }
            "OneTapV2" -> {
                RoundedUtil.drawRound(0f, 0f, Fonts.sfbold45.getStringWidth("GlowClient") + Fonts.fontSFUI45.getStringWidth(
                    " | $username | $ping | $fps"
                ) + 35f, 16f, 1f, Color(28, 30, 40))
                RoundedUtil.drawRound(-0.5f, -1.7f, Fonts.sfbold45.getStringWidth("GlowClient") + Fonts.fontSFUI45.getStringWidth(" | $username | $ping | $fps") + 35f, 1.2f, 0.5f, Color(UIEffects.Hudcolor.getValue()))

                val username: String = mc.session.username
                val ping = EntityUtils.getPing(mc.thePlayer!!).toString()
                val fps = Minecraft.getDebugFPS().toString() + "fps"
                Fonts.sfbold45.drawString("GlowClient", 5, 13-Fonts.sfbold45.fontHeight, -1)
                Fonts.fontSFUI45.drawString(
                    " | $username | $ping Ping | $fps",
                    Fonts.sfbold45.getStringWidth("GlowClient") + 5,
                    13-Fonts.sfbold45.fontHeight,
                    -1
                )
            }
        }

        return Border(0f, 0f, Fonts.sfbold45.getStringWidth("GlowClient") + Fonts.fontSFUI45.getStringWidth(
            " | $username | $ping Ping | $fps"
        ) + 8f, 20f)

    }
    private fun colorText() {
        val username: String = mc.session.username
        val ping = EntityUtils.getPing(mc.thePlayer!!).toString()
        val fps = Minecraft.getDebugFPS().toString() + "fps"
        Fonts.sfbold45.drawString("GlowClient", 5, 13-Fonts.sfbold45.fontHeight, -1)
        Fonts.fontSFUI45.drawString(
            " | $username | $ping Ping | $fps",
            Fonts.sfbold45.getStringWidth("GlowClient") + 5,
            13-Fonts.sfbold45.fontHeight,
            -1
        )
    }
}
