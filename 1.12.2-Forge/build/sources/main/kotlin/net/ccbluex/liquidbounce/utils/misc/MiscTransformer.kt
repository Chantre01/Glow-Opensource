/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
package net.ccbluex.liquidbounce.utils.misc

import com.google.gson.JsonObject
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.LiquidBounce.moduleManager

import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance.mc
import java.text.SimpleDateFormat
import java.util.*

object MiscTransformer {
    @JvmStatic
    var prefix = "»"

    @JvmStatic
    var cname = "GlowClient"

    @JvmStatic
    var ccolor = "§b"


    fun variablesTransform(string: String): String {
        var text = string
        text = text.replace("%L%", "Ｌ")
        text = text.replace("%l%", "Ｌ")
        text = text.replace("%cname%",cname)
        text = text.replace("%cversion%", LiquidBounce.CLIENT_VERSION)
        text = text.replace("%cdev%", LiquidBounce.CLIENT_CREATOR)
        text = text.replace("%kill%", Recorder.killCounts.toString())
        text = text.replace("%win%", Recorder.win.toString())
        text = text.replace("%total%", Recorder.totalPlayed.toString())
        text = text.replace("%ban%", Recorder.ban.toString())
        text = text.replace("%playtime%", getSessionInfoPlayTime(false))

        return text
    }

    fun getSessionInfoPlayTime(spaceBetweenWords: Boolean): String {
        var time: String
        if (spaceBetweenWords) {
            val format = SimpleDateFormat("HH 时 mm 分 ss 秒")
            time = format.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))
        } else {
            val format = SimpleDateFormat("HH时mm分ss秒")
            time = format.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))
            if (time.startsWith("00时0")) time = time.replace("00时0", "")
            if (time.startsWith("00时")) time = time.replace("00时", "")
            if (time.contains("00分")) time = time.replace("00分", "")
            if (time.contains("时0")) time = time.replace("时0", "时")
            if (time.contains("分0")) time = time.replace("分0", "分")
        }
        return time
    }


    fun notificationsTransform(title: String, content: String, type: NotifyType, time: Int) {
        when (moduleManager.toggleMessageMode) {
            1 -> ClientUtils.displayChatMessage("${ccolor}${cname} §7${prefix} §b$title §7» §6$content")
            2 -> LiquidBounce.hud.addNotification(
                Notification(
                    title, content, type, time
                )
            )
        }
    }

    fun displayChatMessage(commandChatMode: Boolean, message: String) {
        if (mc.thePlayer == null) {
            ClientUtils.getLogger().info("(MCChat) $message")
            return
        }

        val jsonObject = JsonObject()
        if (commandChatMode) {
            jsonObject.addProperty(
                "text",
                ("${ccolor}${cname} §7${prefix} " + message)
            )

            mc.thePlayer!!.addChatMessage(LiquidBounce.wrapper.functions.jsonToComponent(jsonObject.toString()))
        } else {
            jsonObject.addProperty("text", message)

            mc.thePlayer!!.addChatMessage(LiquidBounce.wrapper.functions.jsonToComponent(jsonObject.toString()))
        }
    }
}