/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */

package net.ccbluex.liquidbounce.utils

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.utils.misc.HttpUtils.get
import net.ccbluex.liquidbounce.utils.misc.StringUtils
import net.ccbluex.liquidbounce.value.*
import org.lwjgl.input.Keyboard

/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
object OldConfigSupport {

    /**
     * Execute settings [script]
     */
    fun loadConfig(script: String) {
        script.lines().filter { it.isNotEmpty() && !it.startsWith('#') }.forEachIndexed { index, s ->
            val args = s.split(" ").toTypedArray()

            if (args.size <= 1) {
                ClientUtils.displayChatMessage("§c在设置脚本的 §7$index 中出现了格式错误。出现位置: §7第 $s 行。")
                return@forEachIndexed
            }

            when (args[0]) {
                "load" -> {
                    val urlRaw = StringUtils.toCompleteString(args, 1)
                    val url = if (urlRaw.startsWith("http"))
                        urlRaw
                    else
                        "${LiquidBounce.CLIENT_CLOUD}/settings/${urlRaw.toLowerCase()}"

                    try {
                        loadConfig(get(url))
                    } catch (_: Exception) {
                    }
                }

                "targetPlayer", "targetPlayers" -> {
                    EntityUtils.targetPlayer = args[1].equals("true", ignoreCase = true)
                }

                "targetMobs" -> {
                    EntityUtils.targetMobs = args[1].equals("true", ignoreCase = true)
                }

                "targetAnimals" -> {
                    EntityUtils.targetAnimals = args[1].equals("true", ignoreCase = true)
                }

                "targetInvisible" -> {
                    EntityUtils.targetInvisible = args[1].equals("true", ignoreCase = true)
                }

                "targetDead" -> {
                    EntityUtils.targetDead = args[1].equals("true", ignoreCase = true)
                }

                else -> {
                    if (args.size > 3) {
                        ClientUtils.displayChatMessage("§c在配置文件中的第 §7$index §c行出现了格式错误。§f内容: §7$s")
                        return@forEachIndexed
                    }

                    val moduleName = args[0]
                    val valueName = args[1]
                    val value = args[2]
                    val module = LiquidBounce.moduleManager.getModule(moduleName)

                    if (module == null) {
                        ClientUtils.displayChatMessage("§c配置文件中的模块 §7$moduleName §c丢失!")
                        return@forEachIndexed
                    }

                    if (valueName.equals("toggle", ignoreCase = true)) {
                        module.state = value.equals("true", ignoreCase = true)
                        return@forEachIndexed
                    }

                    if (valueName.equals("bind", ignoreCase = true)) {
                        module.keyBind = Keyboard.getKeyIndex(value)
                        return@forEachIndexed
                    }

                    val moduleValue = module.getValue(valueName)
                    if (moduleValue == null) {
                        ClientUtils.displayChatMessage("§c配置文件中的数值 §7$valueName§c 不存在于模块 §7$moduleName §c中。")
                        return@forEachIndexed
                    }

                    try {
                        when (moduleValue) {
                            is BoolValue -> moduleValue.changeValue(value.toBoolean())
                            is FloatValue -> moduleValue.changeValue(value.toFloat())
                            is IntegerValue -> moduleValue.changeValue(value.toInt())
                            is TextValue -> moduleValue.changeValue(value)
                            is ListValue -> moduleValue.changeValue(value)
                        }
                    } catch (e: Exception) {
                        ClientUtils.displayChatMessage("§f${e.javaClass.name}§7(${e.message}) §c在将数值 §a§l$value§c 设置在模块 §7${module.name}§c 的 §7${moduleValue.name}§c 时出现错误。")
                    }
                }
            }
        }

        LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.valuesConfig)
    }
}