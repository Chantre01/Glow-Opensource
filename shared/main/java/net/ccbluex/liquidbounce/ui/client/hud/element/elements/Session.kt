package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import cn.novoline.impl.Fonts
import cn.utils.miku.render.RenderUtil
import cn.utils.miku.render.round.RoundedUtil
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder

import net.ccbluex.liquidbounce.utils.render.RenderUtils
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*


@ElementInfo(name = "Session")
class Session(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
    override fun drawElement(): Border {

        //if (UIEffects.hudblur.get()) {
        //    UIEffects.renderblur(renderX,renderY, 120.0F, 45.0F, 6f)
        //}

        when (UIEffects.hudstyle.get()) {
            "CustomColor","CustomShader","CustomOutlined" -> {
                UIEffects.render(0.0f,0.0f, 120.0F, 45.0F, 10f)
                colorText()
            }
            "OneTapV2" -> {
                RoundedUtil.drawRound(0f, 0f, 80f, (17 + 17).toFloat(), 1f, Color(22, 18, 27))
                RoundedUtil.drawRound(0f, 0f, 80f, 15f, 0f, Color(28, 30, 40))
                RoundedUtil.drawRound(0f, 16f, 80f, 0.7f, 0f, Color(42, 39, 44))
                RoundedUtil.drawRound(-0.5f, -1.7f, 81f, 1.2f, 0.5f, Color(UIEffects.Hudcolor.getValue()))

                cn.novoline.impl.Fonts.CsgoIcon.csgoicon_18.csgoicon_18.drawString(
                    "l", 3f, 6.5f, -1
                )
                cn.novoline.impl.Fonts.SFTHIN.SFTHIN_22.SFTHIN_22.drawString(
                    "|", 17f, 4f, Color(47, 51, 52).rgb
                )
                cn.novoline.impl.Fonts.SFTHIN.SFTHIN_16.SFTHIN_16.drawString(
                    "Indicators", 23f, 5.5f, -1
                )
                var speed: Double = calculateBPS()
                speed = RenderUtil.animate(8.0, Math.min(9.0, speed), 0.05).toFloat().toDouble()
                cn.novoline.impl.Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString("Speed", 20f, 24f, -1)
                cn.novoline.impl.Fonts.Tahoma.Tahoma_14.Tahoma_14.drawString(
                    "[ " + String.format("%.2f", speed) + "b/s ]",
                    (76 - cn.novoline.impl.Fonts.Tahoma.Tahoma_14.Tahoma_14.stringWidth("[ " + String.format("%.2f", speed)
                            + "b/s ]")).toFloat(), 24f,
                    Color(100, 100, 100).rgb
                )
                GL11.glPopMatrix()
                RenderUtil.drawArc(renderX.toFloat()+9f, renderY.toFloat()+26f, 3.5,
                    Color(60, 60, 60).darker().rgb, 180, (360 * 2).toDouble(), 2
                )
                RenderUtil.drawArc(renderX.toFloat()+9f, renderY.toFloat()+26f, 3.5, UIEffects.Hudcolor.getValue(), 180 * 2, 340 + speed * 45, 2
                )
                RenderUtil.resetColor()
                GL11.glPushMatrix()
            }
        }

        return Border(-2f, -2f, 120f, 45f)
    }
    private fun calculateBPS(): Double {
        val bps = Math.hypot(
            mc2.player.posX - mc2.player.prevPosX,
            mc2.player.posZ - mc2.player.prevPosZ
        ) * mc.timer.timerSpeed * 20
        return Math.round(bps * 100.0) / 100.0
    }
    private fun colorText() {
        //Texts
        val fontRenderer = cn.novoline.impl.Fonts.SFTHIN.SFTHIN_25.SFTHIN_25
        Fonts.SFBOLD.SFBOLD_22.SFBOLD_22.drawString("Session Info" , 5, 7, Color.WHITE.rgb)
        Fonts.SFTHIN.SFTHIN_22.SFTHIN_22.drawString("Play Time: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}", 5, 12 + (fontRenderer.height), Color.WHITE.rgb)
        Fonts.SFTHIN.SFTHIN_22.SFTHIN_22.drawString("Kills: " + Recorder.killCounts, 5, 14 + (fontRenderer.height * 2), Color.WHITE.rgb)

    }

}
